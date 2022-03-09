/*
 * Copyright 2019 Xilinx Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
#include <iostream>
#include <opencv2/opencv.hpp>
#include <string>
static int count_num =0;
static int toggle = 0;
static cv::Mat old_image;
cv::Mat process_result(cv::Mat &m1, const vitis::ai::FaceDetectResult &result,
                       bool is_jpeg, int frameid, float videofps, string file_without_extension) {
  // vector<int> compression_params;                       
  cv::Mat image;
  cv::Rect image_save;
  cv::Rect image_save_crop;
  cv::Mat croppedFaceImage;
  cv::Mat result_cmp;
  int CMP_EQ;
  int diff;
  float cur_time = 0;
  cv::resize(m1, image, cv::Size{result.width, result.height});
  // LOG(INFO) << "Process_reusult" << file_without_extension;
  for (const auto &r : result.rects) {
    LOG_IF(INFO, is_jpeg) << " " << r.score << " "  //
                          << r.x << " "             //
                          << r.y << " "             //
                          << r.width << " "         //
                          << r.height;
    cv::rectangle(image, cv::Rect{cv::Point(r.x * image.cols, r.y * image.rows), cv::Size{(int)(r.width * image.cols), (int)(r.height * image.rows)}}, 0xff);
    
    image_save = cv::Rect{cv::Point(r.x * image.cols, r.y * image.rows), cv::Size{(int)(r.width * image.cols), (int)(r.height * image.rows)}};

    if (0 <= image_save.x // box within the image plane
    && 0 < image_save.width
    && image_save.x + image_save.width < image.cols
    && image_save.width < image.cols
    && image_save.height < image.rows
    && image.cols > 0
    && 0 <= image_save.y
    && image_save.y + image_save.height < image.rows
    && image.rows > 0 ){
      // LOG(INFO) << " " << r.score << " "  //
      //                       << image_save.x << " "             //
      //                       << image_save.y << " "             //
      //                       << image_save.width << " "         //
      //                       << image_save.height << " "
      //                       << image.cols << " "
      //                       << image.rows;  

      croppedFaceImage = image(image_save).clone();
      cv::resize(croppedFaceImage, croppedFaceImage,cv::Size(100,100));
      // cv::imshow("Img",croppedFaceImage);
      cur_time = (frameid * 1.000) / videofps;
      std::string path = "/workspace/demo/Vitis-AI-Library/output/"+ file_without_extension + "," + std::to_string(cur_time) + ".jpg";
      // bool check = cv::imwrite(path,croppedFaceImage);
      // LOG(INFO) << check ;
      // count_num++;
      // LOG(INFO) << "************************" << frameid;
      // LOG(INFO) << "************************" << videofps;
      old_image.cols = 100;
      old_image.rows = 100;
      if (toggle == 0){
        toggle = 1;
        old_image = croppedFaceImage.clone();
      }
      if (toggle == 1){
        cv::compare(old_image , croppedFaceImage  , result_cmp , cv::CMP_EQ );
        // cv::absdiff(old_image, croppedFaceImage,result_cmp);
        cv::cvtColor(result_cmp, result_cmp, CV_BGR2GRAY);
        // diff = cv::threshold(result_cmp, result_cmp, 80, 255, cv::THRESH_BINARY);
        // cv::imshow("old_image",old_image);
        // cv::imshow("croppedFaceImage",croppedFaceImage);
        // cv::imshow("DIFF",result_cmp); // recent
        diff = cv::countNonZero(result_cmp);
        // LOG(INFO) << diff; //recent
        old_image = croppedFaceImage.clone();
        if(diff > 3000){
          bool check = cv::imwrite(path,croppedFaceImage);
          count_num++;
        }
      }      
    }
  }
  return image;
}

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

#include <glog/logging.h>

#include <iomanip>
#include <iostream>
#include <fstream>
#include <string>
#include <memory>
#include <opencv2/core.hpp>
#include <opencv2/highgui.hpp>
#include <opencv2/imgproc.hpp>
#include <vector>
#include <vitis/ai/env_config.hpp>
#include <vitis/ai/reid.hpp>

DEF_ENV_PARAM(SAMPLES_ENABLE_BATCH, "0");
DEF_ENV_PARAM(SAMPLES_BATCH_NUM, "0");

using namespace std;
using namespace cv;

double cosine_distance(Mat feat1, Mat feat2) { return 1 - feat1.dot(feat2); }

int main(int argc, char* argv[]) {
  if (argc < 4) {
    cerr << "need at least two images" << endl;
    return -1;
  }
  auto model_name = argv[1];
  auto det = vitis::ai::Reid::create(model_name);
  
  std::string filename_2 = argv[2];
  std::string base_filename_2 = filename_2.substr(filename_2.find_last_of("/\\") + 1);
  std::string::size_type const p2(base_filename_2. find_last_of('.'));
  std::string file_without_extension_2 = base_filename_2. substr(0, p2);

  std::string filename_3 = argv[3];
  std::string base_filename_3 = filename_3.substr(filename_3.find_last_of("/\\") + 1);
  std::string::size_type const p3(base_filename_3. find_last_of('_'));
  std::string file_without_extension_3 = base_filename_3. substr(0, p3);

  if (ENV_PARAM(SAMPLES_ENABLE_BATCH)) {
    std::vector<std::string> image_x_files;
    std::vector<std::string> image_y_files;
    for (int i = 2; i < argc; i = i + 2) {
      image_x_files.push_back(std::string(argv[i]));
    }
    for (int i = 3; i < argc; i = i + 2) {
      image_y_files.push_back(std::string(argv[i]));
    }
    if (image_x_files.empty() || image_y_files.empty()) {
      std::cerr << "no input file" << std::endl;
      exit(1);
    }
    if (image_x_files.size() != image_y_files.size()) {
      std::cerr << "input images should be pair" << std::endl;
      exit(1);
    }

    auto batch = det->get_input_batch();
    if (ENV_PARAM(SAMPLES_BATCH_NUM)) {
      unsigned int batch_set = ENV_PARAM(SAMPLES_BATCH_NUM);
      assert(batch_set <= batch);
      batch = batch_set;
    }

    std::vector<std::string> batch_x_files(batch);
    std::vector<cv::Mat> x_images(batch);
    for (auto index = 0u; index < batch; ++index) {
      const auto& file = image_x_files[index % image_x_files.size()];
      batch_x_files[index] = file;
      x_images[index] = cv::imread(file);
      CHECK(!x_images[index].empty()) << "cannot read image from " << file;
    }
    std::vector<std::string> batch_y_files(batch);
    std::vector<cv::Mat> y_images(batch);
    for (auto index = 0u; index < batch; ++index) {
      const auto& file = image_y_files[index % image_y_files.size()];
      batch_y_files[index] = file;
      y_images[index] = cv::imread(file);
      CHECK(!y_images[index].empty()) << "cannot read image from " << file;
    }
    auto y_results = det->run(x_images);
    auto x_results = det->run(y_images);
    assert(x_results.size() == batch);
    for (auto i = 0u; i < x_results.size(); i++) {
      double dismat = cosine_distance(x_results[i].feat, y_results[i].feat);
      LOG(INFO) << "batch: " << i;
      LOG(INFO) << "distmat : " << std::fixed << std::setprecision(3) << dismat;
      std::cout << std::endl;
    }
  } else {
    Mat imgx = imread(argv[2]);
    if (imgx.empty()) {
      cerr << "can't load image! " << argv[1] << endl;
      return -1;
    }
    Mat imgy = imread(argv[3]);
    if (imgy.empty()) {
      cerr << "can't load image! " << argv[2] << endl;
      return -1;
    }
    Mat featx = det->run(imgx).feat;
    Mat featy = det->run(imgy).feat;
    double dismat = cosine_distance(featx, featy);
    // printf("dismat : %.3lf \n", dismat); //recent
    if(dismat < 0.1){
      // printf("dismat : %.3lf \n", dismat); //recent
      cout << file_without_extension_2 << "," << file_without_extension_3 << endl;
      std::string input = file_without_extension_2 + "," + file_without_extension_3;
      // std::ofstream out("/workspace/demo/Vitis-AI-Library/output/output.txt");
      // out << input;
      // out.close();
      std::ofstream outfile;
      outfile.open("/workspace/demo/Vitis-AI-Library/output/output.txt", std::ios_base::app); // append instead of overwrite
      outfile << input << endl;
      // LOG(INFO) << file_without_extension_2 << "_" << file_without_extension_3;
    }
    else {
      std::string input2 = file_without_extension_2;
      std::ofstream outfile2;
      outfile2.open("/workspace/demo/Vitis-AI-Library/output/output_rem.txt", std::ios_base::app); // append instead of overwrite
      outfile2 << input2 << endl;
    }
  }
  return 0;
}

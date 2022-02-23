#!/bin/bash
# SOURCEDIR=/workspace/demo/Vitis-AI-Library/output
# DESTDIR=/workspace/demo/Vitis-AI-Library/stored_faces
# for i in {0..50}
# do
# #    echo "Welcome $i times"
#    ./test_jpeg_reid reid /workspace/demo/Vitis-AI-Library/output/cropped_$i.jpg /workspace/demo/Vitis-AI-Library/stored_faces/vembu.jpg
# done
for filename in /workspace/demo/Vitis-AI-Library/output/*.jpg
do
   # echo "$filename"
   /workspace/demo/Vitis-AI-Library/samples/reid/test_jpeg_reid reid $filename /workspace/demo/Vitis-AI-Library/stored_faces/vembu.jpg
done
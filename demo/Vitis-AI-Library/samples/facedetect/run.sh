#!/bin/bash

# face detetcion 
./test_video_facedetect densebox_640_360 $1

# face reidentification
# echo "$1" | sed -r "s/.+\/(.+)\..+/\1/"
var1="$1"
xbase=${var1##*/}
path=${xbase%.*}
# echo $path
for filename in /workspace/demo/Vitis-AI-Library/output/$path*.jpg
do
   # echo "$filename"
   for storedfaces in /workspace/demo/Vitis-AI-Library/stored_faces/*.jpg
   do
      /workspace/demo/Vitis-AI-Library/samples/reid/test_jpeg_reid reid $filename $storedfaces
   done
done

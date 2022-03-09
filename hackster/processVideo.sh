#!/bin/sh
INPUTFILE=$1
CONTAINERID=$(docker ps | grep -i 'xilinx/vitis-ai' | awk {'print $1'})

docker exec -t --user vitis-ai-user $CONTAINERID bash -c "cd /workspace/demo/Vitis-AI-Library/samples/facedetect/ && source /workspace/setup/vck5000/setup.sh && ./facedetect.sh /workspace/demo/Vitis-AI-Library/input/$INPUTFILE"
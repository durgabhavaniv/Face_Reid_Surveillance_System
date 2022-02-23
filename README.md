# Face_Reid_Surveillance_System
Near Real-Time Face-reIdentification Surveillance System

# Install latest version of docker
 - [Install Docker](docs/install_docker/README.md) - if Docker not installed on your machine yet
 - [Ensure your linux user is in the group docker](https://docs.docker.com/install/linux/linux-postinstall/)

# Download vitis-ai docker
```
docker pull xilinx/vitis-ai:1.4.1.978 
```

# Clone this repository
```bash
git clone --recurse-submodules https://github.com/durgabhavaniv/Face_Reid_Surveillance_System.git

cd Face_Reid_Surveillance_System
```

# Initial setup outside docker
```bash
cd ~/Face_Reid_Surveillance_System/setup/vck5000

source ./install.sh
```

# Run docker
```
./docker_run.sh xilinx/vitis-ai:1.4.1.978
```

# Update the stored faces data
```
cd ~/Face_Reid_Surveillance_System/demo/Vitis-AI-Library/stored_faces/
```

# Follow below setup commands inside docker
```bash
sudo apt-get update

sudo apt-get install libcanberra-gtk-module -y

source /workspace/setup/vck5000/setup.sh

sudo mkdir /usr/share/vitis_ai_library/models

cd /workspace/demo/Vitis-AI-Library/samples/reid

sudo cp reid /usr/share/vitis_ai_library/models -r

cd /workspace/demo/Vitis-AI-Library/samples/facedetect

sudo cp densebox_640_360 /usr/share/vitis_ai_library/models -r
```

# Run person reidentification demo using following commads
```bash
./facedetect.sh /workspace/demo/Vitis-AI-Library/input/Camera1.mp4 &
./facedetect.sh /workspace/demo/Vitis-AI-Library/input/Camera2.mp4 &
./facedetect.sh /workspace/demo/Vitis-AI-Library/input/Camera2.mp4 &
./facedetect.sh /workspace/demo/Vitis-AI-Library/input/Camera4.mp4 &
```

# Results
 - ~/Face_Reid_Surveillance_System/demo/Vitis-AI-Library/output this directory givens the deetcted faces results.
 - ~/Face_Reid_Surveillance_System/demo/Vitis-AI-Library/output/output.txt gives results of tracking of person faces with respect to camera number and time.

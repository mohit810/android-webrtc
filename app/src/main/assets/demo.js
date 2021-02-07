let localVideo = document.getElementById("local-video")
let remoteVideo = document.getElementById("remote-video")

const hdConstraints = {
    audio: true,
    video: {
        width: { max: 1920, ideal: 1280 },
        height: { max: 1080, ideal: 720 }
    }
};
let localStream
function init() {
       navigator.getUserMedia({
                  audio: true,
                  video: true
              }, (stream) => {
                  localVideo.srcObject = stream
                  localStream = stream

                  call.answer(stream)
                  call.on('stream', (remoteStream) => {
                      remoteVideo.srcObject = remoteStream

                      remoteVideo.className = "primary-video"
                      localVideo.className = "secondary-video"

                  })

              })
}
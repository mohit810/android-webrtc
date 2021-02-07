let localVideo = document.getElementById("local-video")

let streamKey, encryptedSdp, remoteSessionDescription

const hdConstraints = {
    audio: true,
    video: {
        width: { max: 1920, ideal: 1280 },
        height: { max: 1080, ideal: 720 }
    }
};

function postRequest() {
    var data = JSON.stringify({
        "sdp": encryptedSdp,
        "streamKey": streamKey
    })
    console.log(data);
    const url = "http://www.website.com:8080/sdp";
    (async () => {
        const rawResponse = await fetch(url, {
            method: "POST",
            body: data,
            headers: {
                'Content-Type': 'application/json'
            }
        });
        const content = await rawResponse.json();
        remoteSessionDescription = content.sdp
        window.startSession()
    })();
}

function init(uid) {

    streamKey = uid
    let pc = new RTCPeerConnection()
    pc.oniceconnectionstatechange = e => log(pc.iceConnectionState)
    pc.onicecandidate = event => {
        if (event.candidate === null) {
            encryptedSdp = btoa(JSON.stringify(pc.localDescription))
            postRequest();
        }
    }

    navigator.getUserMedia(hdConstraints, (stream) => {
            localVideo.srcObject = stream
            stream.getTracks().forEach(function (track) {
                pc.addTrack(track, stream);
            });
            pc.createOffer()
                .then(d => {
                    pc.setLocalDescription(d)
                })
        })

}

function startSession () {
    let sd = remoteSessionDescription
    if (sd === '') {
        return alert('Session Description must not be empty')
    }
    try {
        pc.setRemoteDescription(new RTCSessionDescription(JSON.parse(atob(sd))))
    } catch (e) {
        alert(e)
    }
}
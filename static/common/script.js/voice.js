

async function sendAudio() {
    try {
        //console.log("sending ", audioString);
        const response = await post("/send-audio", {
            userid: userID,
            audioData: audioString
        }, 'application/json');
    } catch (error) {
        console.error(error);
    }
}
    
    
async function getAudio() {
    try {
        const response = await post("/get-audio", {
        userid: userID
        }, 'application/json');
        
        serverAudio = response.audioData;
        //console.log("playing ", serverAudio);
        playAllAudio(serverAudio);

    } catch (error) {
        console.error(error);
    }
}
    
    
let mediaRecorder;
let audioChunks = [];
let isRecording = false;
let audioBlob = null;
let audioString = null;
let serverAudio = null;

    
function startRecording() {
    // Clear the previous recording data
    audioChunks = [];
    audioBlob = null;
    audioString = null;

    navigator.mediaDevices.getUserMedia({ audio: true })
        .then((stream) => {
            mediaRecorder = new MediaRecorder(stream);
            mediaRecorder.ondataavailable = (event) => {
                if (event.data.size > 0) {
                    audioChunks.push(event.data);
                }
            };
            mediaRecorder.start();
            isRecording = true;
        })
        .catch((error) => {
            console.error('Error accessing microphone:', error);
        });
}

function stopRecording() {
    if (mediaRecorder && isRecording) {
        mediaRecorder.stop();
        mediaRecorder.onstop = () => {
            isRecording = false;

            if (audioChunks.length > 0) {
                audioBlob = new Blob(audioChunks, { type: 'audio/wav' });

                // Convert the Blob to a base64 string
                const reader = new FileReader();
                reader.onloadend = () => {
                    audioString = reader.result;
                    sendAudio();
                };
                reader.readAsDataURL(audioBlob);
            }
        };
    }
}

function playAllAudio(audioList) {
    for (item of audioList) {
        if (item != "") {
            const audioElement = new Audio(item);
            audioElement.play();
        }
    }
}


// Change the state of the mic button based on whether it is toggled. Add voice chat stuff here later.
const micButton = document.getElementById('micButton');
let isMuted = false; // You can change this based on your actual state

/*micButton.addEventListener('click', () => {
    isMuted = !isMuted;
    micButton.classList.toggle('muted');
    
    if (recognition) {
		if (isMuted) {
        	stopRecRecording();
	    } else {
	        startRecRecording();
	    }
	}
    
});
*/
function myTimerFunction() {
    if (isMuted === false) {
        stopRecording();
        startRecording();
    } else {
        audioString = "";
        sendAudio();
    }
}


/*    
let isRecognitionRecording;
let transcript = '';
let wordCountDict = {};

const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
const recognition = new SpeechRecognition();
recognition.lang = 'en-US';

recognition.onstart = function () {
	console.log("starting");
    isRecognitionRecording = true;
};

recognition.onend = function () {
	console.log("ending");
    isRecognitionRecording = false;
    updateWordCount();
    
    if (!isMuted) {
        startRecognitionRecording(); // Start recording again after it ends
    }
    
};

recognition.onresult = function (event) {
    const lastResult = event.results[event.results.length - 1][0];
    transcript += lastResult.transcript + ' ';
};






function startRecognitionRecording() {
    recognition.start();
}

function stopRecognitionRecording() {
    recognition.stop();
}

function updateWordCount() {
    const words = transcript
        .toLowerCase()
        .split(/\s+/)
        .filter(word => word.match(/\w/));

    words.forEach(word => {
        if (wordCountDict[word]) {
            wordCountDict[word]++;
        } else {
            wordCountDict[word] = 1;
        }
    });
    transcript = ''; // Clear transcript
}
*/





startRecording();

//startRecognitionRecording();


// Set up the timer to call myTimerFunction every 10 milliseconds
const timerIntervalVoice = 500; // 0 milliseconds
const timer = setInterval(myTimerFunction, timerIntervalVoice);
const timer2 = setInterval(getAudio, timerIntervalVoice);



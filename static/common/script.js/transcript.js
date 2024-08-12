let isRecRecording = false;
let transcript = '';
let wordCountDict = {};

const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
const recognition = new SpeechRecognition();
recognition.lang = 'en-US';

recognition.onstart = function () {
    isRecRecording = true;
};

recognition.onend = function () {
    isRecRecording = false;
    updateWordCount();
    if (isMuted === false) {
		startRecRecording();
	}
     // Start recording again after it ends
};

recognition.onresult = function (event) {
    const lastResult = event.results[event.results.length - 1][0];
    transcript += lastResult.transcript + ' ';
};



function startRecRecording() {
    recognition.start();
}

function stopRecRecording() {
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

startRecRecording();

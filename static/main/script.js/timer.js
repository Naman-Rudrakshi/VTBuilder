//variables and constants for numbers and elements
let timerInterval;
let seconds = 0;
let minutes = 0;

const minutesDisplay = document.getElementById('minutes');
const secondsDisplay = document.getElementById('seconds');

//idk what this does exactly but it updates the numbers
function updateTimerDisplay() {
    minutesDisplay.textContent = minutes.toString().padStart(2, '0');
    secondsDisplay.textContent = seconds.toString().padStart(2, '0');
}

//starts the timer. called at the start of the game
function startTimer() {
    timerInterval = setInterval(() => {
        seconds++;
        if (seconds === 60) {
            seconds = 0;
            minutes++;
        }
        updateTimerDisplay();
    }, 1000);

}

//called at the end of the game. time is sent to server
function stopTimer() {
    clearInterval(timerInterval);
    return (minutes*60 + seconds);
}

// Initial display of timer
updateTimerDisplay();

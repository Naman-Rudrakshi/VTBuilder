const text1 = document.getElementById('loading');
const text2 = document.getElementById('description');

const originalText = 'Fetching Results.';
let textBankIndex = 0;

function updateText1() {
    const currentText = text1.textContent;
    if (currentText.length < originalText.length + 3) {
        text1.textContent = currentText + '.';
    } else {
        text1.textContent = originalText;
    }
}

function startAnimation() {     
    updateFontSize();
    interval1 = setInterval(updateText1, 500);
}

function endAnimation() {
    clearInterval(interval1);
}












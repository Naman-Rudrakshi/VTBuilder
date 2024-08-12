//done
updateFontSize();
document.getElementById('save-button').addEventListener('click', function () {
    slider1Value = document.getElementById('slider1').value;
    slider2Value = document.getElementById('slider2').value;

    showPage(2);
    //showPage(2);
    startAnimation();
    sendSurveyData();

    // Here, you can send the data to a server or perform any other necessary action.
});

// Show the first popup initially
showPopup(1,4);

const element = document.getElementById('cloudTitle');

    // Get the popup element by ID
const popup = document.getElementById('info-popup1');

// Show the popup next to the element when hovering over the element
element.addEventListener('mouseover', function (event) {
    // Get the position of the element
    const elementRect = element.getBoundingClientRect();
    const elementX = elementRect.right + 10+ window.scrollX;
    const elementY = elementRect.top -100+ (window.scrollY);

    // Position the popup next to the element
    popup.style.display = 'block';
    popup.style.top = elementY + 'px';
    popup.style.left = elementX + 'px';
    console.log("entered");
});

// Hide the popup when the mouse leaves the element
element.addEventListener('mouseout', function () {
    popup.style.display = 'none';
});


const element2 = document.getElementById('weights');

// Get the popup element by ID
const popup2 = document.getElementById('info-popup2');

// Show the popup next to the element when hovering over the element
element2.addEventListener('mouseover', function (event) {
    // Get the position of the element
    const elementRect = element2.getBoundingClientRect();
    const elementX = elementRect.right + 10+ window.scrollX;
    const elementY = elementRect.top -100+ (window.scrollY);

    // Position the popup next to the element
    popup2.style.display = 'block';
    popup2.style.top = elementY + 'px';
    popup2.style.left = elementX + 'px';
    console.log("entered");
});

// Hide the popup when the mouse leaves the element
element2.addEventListener('mouseout', function () {
    popup2.style.display = 'none';
});

    const element3 = document.getElementById('counts');

    // Get the popup element by ID
    const popup3 = document.getElementById('info-popup3');

    // Show the popup next to the element when hovering over the element
    element3.addEventListener('mouseover', function (event) {
        // Get the position of the element
        const elementRect = element3.getBoundingClientRect();
        const elementX = elementRect.right + 10+ window.scrollX;
        const elementY = elementRect.top -100+ (window.scrollY);

        // Position the popup next to the element
        popup3.style.display = 'block';
        popup3.style.top = elementY + 'px';
        popup3.style.left = elementX + 'px';
        console.log("entered");
    });

    // Hide the popup when the mouse leaves the element
    element3.addEventListener('mouseout', function () {
        popup3.style.display = 'none';
    });
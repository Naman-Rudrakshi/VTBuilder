//still need to code dynamic text resizing. unecessary for now

// JavaScript functions to show and close popups
function showPopup(popupNumber, num) {
    // Hide all popups
    for (let i = 1; i <= num; i++) {
        document.getElementById(`popup-${i}`).style.display = 'none';
    }

    // Show the selected popup
    document.getElementById(`popup-${popupNumber}`).style.display = 'flex';

    // Show the overlay
    document.getElementById('popup-overlay').style.display = 'flex';
}

function closePopup(num) {
    // Hide the overlay and all popups
    for (let i = 1; i <= num; i++) {
        document.getElementById(`popup-${i}`).style.display = 'none';
    }
    document.getElementById('popup-overlay').style.display = 'none';
}




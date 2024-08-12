
// JavaScript functions to show and close popups
function showPage(pageNumber) {
    // Hide all popups
    for (let i = 1; i <= 4; i++) {
        document.getElementById(`page-${i}`).style.display = 'none';
    }

    // Show the selected popup
    document.getElementById(`page-${pageNumber}`).style.display = 'flex';
    updateFontSize();
    updateLayout();

}

function closePage() {
    // Hide the overlay and all popups
    for (let i = 1; i <= 4; i++) {
        document.getElementById(`page-${i}`).style.display = 'none';
    }
    
}

// Show the first popup initially
showPage(1);
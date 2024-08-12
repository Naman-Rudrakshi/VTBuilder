function findCategory(word) {
    let weight = impactDict[word]/wordCountDict[word];
    let i=0;
    for (cat of categorizedWeightsValues) {
        if (cat === weight) {
            return categorizedWeightsKeys[i];
        }
        i++;
    } 
}

const cloudContainer = d3.select("#wordCloud");
const tooltipBox = document.getElementById("cloudTooltipBox");
const containerWidth = cloudContainer.node().getBoundingClientRect().width;
const containerHeight = cloudContainer.node().getBoundingClientRect().height;





function drawWordCloud(words) {
    cloudContainer.selectAll("span").remove();

    const wordElements = cloudContainer.selectAll("span")
        .data(words)
        .enter()
        .append("span")
        .text(d => d.text.toUpperCase())
        .style("font-size", d => d.size + "px")
        .style("color", d => d.sign > 0 ? "green" : "red")
        .style("position", "absolute")
        .style("transform", d => `translate(${d.x}px, ${d.y}px)`)
        .style("font-family", "Impact");

    // Add event listeners to show and hide the tooltip box
    wordElements.on("mouseenter", function (d) {
        const rect = this.getBoundingClientRect();
        const lowercaseWord = this.textContent.toLowerCase(); // Get the text content of the hovered element

        // Check if the word exists in all of the dictionaries
    if (impactDict.hasOwnProperty(lowercaseWord) &&
        wordCountDict.hasOwnProperty(lowercaseWord)) {

        tooltipBox.innerHTML = `
            <p>Word: ${lowercaseWord}</p>
            <p>Impact: ${impactDict[lowercaseWord]}</p>
            <p>Usages: ${wordCountDict[lowercaseWord]}</p>
            <p>Category: ${findCategory(lowercaseWord)}</p>
        `;

        tooltipBox.style.top = rect.bottom + "px";
        tooltipBox.style.left = rect.left + "px";
        tooltipBox.style.display = "block";
    }
    });

    wordElements.on("mouseleave", function () {
        tooltipBox.style.display = "none";
    });
}



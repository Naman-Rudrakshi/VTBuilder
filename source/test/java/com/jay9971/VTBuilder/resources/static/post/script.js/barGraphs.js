function dictionariesAreEqual(dict1, dict2) {
  // Check if the number of keys is the same
  const keys1 = Object.keys(dict1);
  const keys2 = Object.keys(dict2);

  if (keys1.length !== keys2.length) {
    return false;
  }

  // Check if the keys and values are the same
  for (const key of keys1) {
    if (dict1[key] !== dict2[key]) {
      return false;
    }
  }

  return true;
}

function combineListsToDictionary(keys, values) {
    if (keys.length !== values.length) {
        throw new Error("The two lists must have the same length.");
    }

    const combinedDictionary = {};

    for (let i = 0; i < keys.length; i++) {
        combinedDictionary[keys[i]] = values[i];
    }

    return combinedDictionary;
}


// Combine the lists into a dictionary


console.log(window.innerWidth);
const width = 0.36 * window.innerWidth;
console.log(width);
const height = 0.324 * window.innerHeight/1.2;



const margin = { top: 20, right: 20, bottom: 30, left: 40 };

function createBarGraph(containerId, data, graphTitle) {
  const svg = d3.select(`#${containerId}`)
    .append("svg")
    .attr("width", width)
    .attr("height", height);

  // Calculate the minimum and maximum values in the dataset
  const minValue = d3.min(Object.values(data));
  const maxValue = d3.max(Object.values(data));

  const x = d3.scaleBand()
    .domain(Object.keys(data))
    .range([margin.left, width - margin.right])
    .padding(0.1);

  const y = d3.scaleLinear()
    .domain([Math.min(0, minValue), Math.max(0, maxValue)]) // Adjust domain for negative values
    .nice()
    .range([height - margin.bottom, margin.top]);

  const tooltip = d3.select("body")
    .append("div")
    .attr("class", "tooltip")
    .style("display", "none");

  const bars = svg.selectAll("rect")
    .data(Object.entries(data))
    .enter()
    .append("rect")
    .attr("x", d => x(d[0]))
    .attr("y", d => {
      if (d[1] >= 0) {
        return y(d[1]);
      } else {
        return y(0); // For negative values, position at the baseline (y=0)
      }
    })
    .attr("height", d => Math.abs(y(0) - y(d[1]))) // Use absolute height
    .attr("width", x.bandwidth())
    .attr("fill", "steelblue");

  bars
    .on("mouseover", function (event, d) {
      
      
      d3.select(this).attr("fill", "orange");
      tooltip.style("display", "block");
      if (dictionariesAreEqual(data,catWeightDict)) {
        tooltip.html(`
            <div>Category: ${d[0]}</div>
            <div>Importance: ${data[d[0]]}</div>
            <div>Line 3 text here</div>
            <p>Short paragraph text goes here.</p>
        `)
        .style("left", (event.pageX - 50) + "px")
        .style("top", (event.pageY + 5) + "px");
      } else {
        tooltip.html(`
            <div>Category: ${d[0]}</div>
            <div>Words Used: ${data[d[0]]}</div>
            <div>Line 3 text here</div>
            <p>Short paragraph text goes here.</p>
        `)
        .style("left", (event.pageX - 50) + "px")
        .style("top", (event.pageY + 5) + "px");
      }
      
    })
    .on("mouseout", function () {
      d3.select(this).attr("fill", "steelblue");
      tooltip.style("display", "none");
    });

  svg.append("g")
    .attr("transform", `translate(0,${height - margin.bottom})`)
    //.call(d3.axisBottom(x));

  svg.selectAll(".domain, .tick line").remove();

  // Add the graph title below the graph
  svg.append("text")
    .attr("x", width / 2)
    .attr("y", height + 20) // Adjust the value to set the title's position
    .text(graphTitle)
    .attr("text-anchor", "middle");
}


const token =
    localStorage.getItem("token");

if(!token){

    window.location.href =
        "login.html";
}

async function fetchIncidents(){

    try{

        const response =
            await fetch(
                "http://localhost:8080/api/incidents",

                {
                    headers:{
                        "Authorization":
                            `Bearer ${token}`
                    }
                }
            );

        const incidents =
            await response.json();

            allIncidents = incidents;

            renderIncidents(incidents);

    }catch(error){

        console.error(error);

        alert(
            "Failed to load incidents"
        );
    }
}

function logout(){

    localStorage.removeItem("token");

    window.location.href =
        "login.html";
}

function goToCreatePage(){

    window.location.href =
        "create.html";
}

function searchIncidents(){

    const keyword =
        document.getElementById(
            "searchInput"
        ).value.toLowerCase();

    const filtered =
        allIncidents.filter(incident =>

            incident.title
                .toLowerCase()
                .includes(keyword)

            ||

            incident.description
                .toLowerCase()
                .includes(keyword)
        );

    renderIncidents(filtered);
}


async function toggleAnalysis(id){

    const analysisDiv =
        document.getElementById(
            `analysis-${id}`
        );

    // collapse if already open
    if(analysisDiv.style.display === "block"){

        analysisDiv.style.display =
            "none";

        return;
    }

    analysisDiv.style.display =
        "block";

    // avoid refetching repeatedly
    if(analysisDiv.innerHTML.trim() !== ""){

        return;
    }

    analysisDiv.innerHTML =
        "<p style='color:#00c6ff;'>Analyzing incident with AI...</p>";

    try{

        const response =
            await fetch(
                `http://localhost:8080/api/incidents/${id}/analyze`,

                {
                    method:"POST",

                    headers:{
                        "Authorization":
                            `Bearer ${token}`
                    }
                }
            );

        const data =
            await response.json();

        let formatted =
            data.aiAnalysis

                .replace(/### (.*?)/g,
                    "<h3>$1</h3>")

                .replace(/\*\*(.*?)\*\*/g,
                    "<b>$1</b>")

                .replace(/\n/g,"<br>");

        analysisDiv.innerHTML =
            formatted;

    }catch(error){

        console.error(error);

        analysisDiv.innerHTML =
            "AI analysis failed";
    }
}

let allIncidents = [];

function renderIncidents(incidents){

    const incidentGrid =
        document.getElementById(
            "incidentGrid"
        );

    incidentGrid.innerHTML = "";

    incidents.forEach(incident => {

        const card =
            document.createElement("div");

        card.classList.add(
            "incident-card"
        );

        card.innerHTML = `
            <h3>${incident.title}</h3>

            <p>${incident.description}</p>

            <div class="severity ${incident.severity}">
                ${incident.severity}
            </div>

            <br><br>

            <button onclick="toggleAnalysis(${incident.id})">
                Analyze with AI
            </button>

            <br><br>

            <button
                onclick="deleteIncident(${incident.id})"

                style="
                    background:#ff4d4d;
                ">
                Delete Incident
            </button>

            <div id="analysis-${incident.id}"
                 class="analysis-box"
                 style="display:none;">
            </div>
        `;

        incidentGrid.appendChild(card);
    });
}

fetchIncidents();

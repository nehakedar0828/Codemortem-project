const token =
    localStorage.getItem("token");

if(!token){

    window.location.href =
        "login.html";
}

const incidentForm =
    document.getElementById("incidentForm");

incidentForm.addEventListener(
    "submit",

    async function(e){

        e.preventDefault();

        const title =
            document.getElementById("title").value;

        const description =
            document.getElementById("description").value;

        const affectedService =
            document.getElementById("affectedService").value;

        const severity =
            document.getElementById("severity").value;

        try{

            const response =
                await fetch(
                    "http://localhost:8080/api/incidents",

                    {
                        method:"POST",

                        headers:{
                            "Content-Type":
                                "application/json",

                            "Authorization":
                                `Bearer ${token}`
                        },

                        body:JSON.stringify({

                            title,
                            description,
                            affectedService,

                            severity,

                            status:"OPEN"
                        })
                    }
                );

            if(response.ok){

                alert(
                    "Incident created successfully!"
                );

                window.location.href =
                    "frontend_dashboard.html";

            }else{

                alert(
                    "Failed to create incident"
                );
            }

        }catch(error){

            console.error(error);

            alert(
                "Something went wrong"
            );
        }
    }
);
const loginForm =
    document.getElementById("loginForm");

if(loginForm){

    loginForm.addEventListener(
        "submit",

        async function(e){

            e.preventDefault();

            const email =
                document.getElementById("email").value;

            const password =
                document.getElementById("password").value;

            try{

                const response =
                    await fetch(
                        "http://localhost:8080/api/auth/login",

                        {
                            method:"POST",

                            headers:{
                                "Content-Type":
                                    "application/json"
                            },

                            body:JSON.stringify({
                                email,
                                password
                            })
                        }
                    );

                const data =
                    await response.json();

                if(response.ok){

                    localStorage.setItem(
                        "token",
                        data.token
                    );

                    alert("Login successful!");

                    window.location.href =
                        "frontend_dashboard.html";

                }else{

                    alert(
                        data.message ||
                        "Login failed"
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
}
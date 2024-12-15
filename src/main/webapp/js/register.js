document.getElementById("registration-form").addEventListener('submit', async function (event) {
    event.preventDefault();

    const errorMessage = document.getElementById('error-message')

    console.log('inside the event listener')

    const formData = new FormData(this);

    try {
        const response = await fetch(this.action, {
            method: this.method,
            body: new URLSearchParams(formData)
        })

        const result = await response.json()

        if (result.status === 'success') {
            alert("User created successfully. You can now login")
            window.location.href = "index.html"
        } else {
            errorMessage.innerText = result.message;
        }
    } catch (error) {
        console.error("Unexpected error occured")
        errorMessage.innerText = "Unexpected Error occured"
    }
});

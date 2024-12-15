document.getElementById('add-product-form').addEventListener('submit', function(event){
    event.preventDefault()

    const formData = new FormData(this)
    fetch('/online-shopping-app/api/admin/addProduct', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(result => {
        if (result.status === 'success') {
            alert(result.message)
            window.location.href = 'manage-products.html'
        } else {
            alert(result.message)
        }
    })
    .catch(error => {
        console.log('Error creating a product')
    })
})
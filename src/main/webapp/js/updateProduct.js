window.onload = function() {
    const URLParams = new window.URLSearchParams(window.location.search)
    const productId = URLParams.get('productId')
    const form = document.getElementById('update-product-form')

    fetch(`/online-shopping-app/api/admin/getProductDetails?productId=${productId}`)
        .then(response => response.json())
        .then(product => {
            console.log(product)
            document.getElementById('productId').value = product.productId
            document.getElementById('productName').value = product.productName
            document.getElementById('productDescription').value = product.productDescription
            document.getElementById('productPrice').value = product.productPrice

            const imageUrl = `data:image/jpeg;base64,${product.productImage}`;
            document.getElementById('productImage').src = imageUrl;

            form.addEventListener('submit', function(event) {
                event.preventDefault()

                const formData = new FormData(this)
                console.log(formData)
                fetch('/online-shopping-app/api/admin/updateProduct', {
                    method: 'PUT',
                    body: formData
                })
                .then(response => response.json())
                .then(result => {
                    if (result.status === 'success') {
                        alert(result.message)
                        console.log(formData)
                        // window.location.href = 'manage-products.html'
                    } else {
                        alert(result.message)
                    }
                }).catch(error => {
                    console.log('Error updating product', error)
                })
            })
        }).catch(error => {
            console.log(error)
        })
    
}
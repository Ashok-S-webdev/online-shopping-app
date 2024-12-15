document.addEventListener('DOMContentLoaded', function() {
    loadProducts()
})

async function removeProduct(productId) {
    try {
        const response = await fetch(`/online-shopping-app/api/admin/removeProduct?productId=${productId}`, {
            method: "DELETE"
        });

        const result = await response.json();
        if (result.status === "success") {
            console.log("success")
            alert(result.message);
            loadProducts();  
        } else {
            console.log('failed')
            alert(result.message);
        }
    } catch (error) {
        console.error("Error removing product from cart:", error);
        alert("An error occurred while removing the product from the cart.");
    }
}


async function loadProducts() {
    try {
        const response = await fetch('/online-shopping-app/api/admin/getProducts', {
            method: 'GET'
        })

        const products = await response.json()
        const productList = document.getElementById('product-list')
        productList.innerHTML = ''
        products.forEach(product => {
            const productDiv = document.createElement('div');
            const imageUrl = `data:image/jpeg;base64,${product.productImage}`;
            productDiv.classList.add('product')
            productDiv.innerHTML = `
            <img src="${imageUrl}" alt="${product.productName}" />
            <h3>${product.productName}</h3>
            <p>${product.productDescription}</p>
            <p>&#8377;${product.productPrice}</p>
            <div class="button-group">
                <button class="remove-btn" data-id="${product.productId}">Remove</button>
                <button class="update-btn" data-id="${product.productId}">Update</button>
            </div>
            `
            productList.appendChild(productDiv);
        });

        document.querySelectorAll('.remove-btn').forEach(button => {
            button.addEventListener('click', function() {
                const productId = this.getAttribute('data-id')
                removeProduct(productId);
            })
        })

        document.querySelectorAll('.update-btn').forEach(button => {
            button.addEventListener('click', function() {
                const productId = this.getAttribute('data-id')
                window.location.href = `update-product.html?productId=${productId}`
            })
        })
    } catch (e) {
        console.error("An error occured: ", e)
    }
}


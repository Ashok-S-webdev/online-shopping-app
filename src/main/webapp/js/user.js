let currentPage = 1
const pageSize = 4

fetch('/online-shopping-app/api/user/info')
.then(response => response.json())
.then(data => {
    document.getElementById('heading').innerHTML = `Welcome ${data.username.charAt(0).toUpperCase() + data.username.slice(1)}`
})

document.addEventListener('DOMContentLoaded', function() {
    fetchProducts(currentPage);
});

async function fetchProducts(page) {
    fetch(`/online-shopping-app/api/user/products?page=${page}`)
        .then(response => response.json())
        .then(data => {
            const productList = document.getElementById('product-list');
            const products = data.products
            const totalPages = data.totalPages
            currentPage = data.currentPage
            productList.innerHTML = ''
            
            products.forEach(product => {
                const productDiv = document.createElement('div');
                const ImageUrl = `data:image/jpeg;base64,${product.productImage}`
                productDiv.classList.add('product');
                productDiv.innerHTML = `
                    <img src="${ImageUrl}" alt="${product.productName}" />
                    <h3>${product.productName}</h3>
                    <p>${product.productDescription}</p>
                    <p>Price: &#8377;${product.productPrice}</p>
                    <button class="add-to-cart" data-id="${product.productId}">Add to Cart</button>
                `;
                productList.appendChild(productDiv);
            });

            renderPagination(currentPage, totalPages)
    
            document.querySelectorAll('.add-to-cart').forEach(button => {
                button.addEventListener('click', function() {
                    const productName = this.getAttribute('data-id');
                    addToCart(productName);
                });
            });

        })
        .catch(error => console.error('Error fetching products:', error));

}

function renderPagination(currentPage, totalPages) {
    const paginationContainer = document.getElementById('pagination')

    paginationContainer.innerHTML = ''

    const prevButton = document.createElement('button');
    prevButton.innerText = 'Prev';
    prevButton.disabled = currentPage === 1;
    if (prevButton.disabled) {
        prevButton.style.backgroundColor = '#b0b0b0'
    }
    prevButton.addEventListener('click', () => {
        currentPage--;
        fetchProducts(currentPage);
    });
    paginationContainer.appendChild(prevButton);

    const pageDiv = document.createElement('span')
    pageDiv.innerHTML = currentPage
    paginationContainer.appendChild(pageDiv)

    const nextButton = document.createElement('button');
    nextButton.innerText = 'Next';
    nextButton.disabled = currentPage === totalPages;
    if (nextButton.disabled) {
        nextButton.style.backgroundColor = '#b0b0b0'
    }
    nextButton.addEventListener('click', () => {
        currentPage++;
        fetchProducts(currentPage);
    });
    paginationContainer.appendChild(nextButton);
}

async function addToCart(productId) {
    console.log(productId)
    try {
        const response = await fetch("/online-shopping-app/api/user/addToCart", {
            method: "POST",
            body: new URLSearchParams({
                action: "add",
                productId: productId
            })
        });

        const result = await response.json();
        if (result.status === "success") {
            alert(result.message);
        } else {
            alert(result.message);
        }
    } catch (error) {
        console.error("Error adding product to cart:", error);
        alert("An error occurred while adding the product to the cart.");
    }
}
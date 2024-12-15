async function incrementQuantity(cartItemId) {
    try {
        const response = await fetch("/online-shopping-app/api/cart/update", {
            method: "PUT",
            body: new URLSearchParams({
                cartItemId: cartItemId,
                quantityChange: 1
            })
        });

        const result = await response.json();
        if (result.status === "success") {
            loadCart(); 
        } else {
            alert(result.message);
        }
    } catch (error) {
        console.error("Error incrementing product quantity:", error);
        alert("An error occurred while incrementing the product quantity.");
    }
}

async function decrementQuantity(cartItemId) {
    try {
        const response = await fetch("/online-shopping-app/api/cart/update", {
            method: "PUT",
            body: new URLSearchParams({
                cartItemId: cartItemId,
                quantityChange: -1
            })
        });

        const result = await response.json();
        if (result.status === "success") {
            loadCart();  
        } else {
            alert(result.message);
        }
    } catch (error) {
        console.error("Error decrementing product quantity:", error);
        alert("An error occurred while decrementing the product quantity.");
    }
}

async function removeFromCart(cartItemId) {
    try {
        const response = await fetch("/online-shopping-app/api/cart/remove", {
            method: "DELETE",
            body: new URLSearchParams({
                cartItemId: cartItemId
            })
        });

        const result = await response.json();
        if (result.status === "success") {
            alert(result.message);
            loadCart();  
        } else {
            alert(result.message);
        }
    } catch (error) {
        console.error("Error removing product from cart:", error);
        alert("An error occurred while removing the product from the cart.");
    }
}

async function checkout() {
    try {
        const response = await fetch("/online-shopping-app/api/cart/checkout", {
            method: "POST",
        });

        const result = await response.json();
        if (result.status === "success") {
            alert(result.message);
            loadCart(); 
        } else {
            alert(result.message);
        }
    } catch (error) {
        console.error("Error during checkout:", error);
        alert("An error occurred during checkout.");
    }
}

async function loadCart() {
    try {
        const response = await fetch("/online-shopping-app/api/cart/cartItems", {
            method: "GET"
        });

        const cartData = await response.json();

        const cartContainer = document.getElementById("cart-items");
        cartContainer.innerHTML = "";  

        let totalPrice = 0;
        cartData.forEach(item => {
            const productDiv = document.createElement("div");
            productDiv.classList.add("cart-item");
            const imageUrl = `data:image/jpeg;base64,${item.productImage}`

            productDiv.innerHTML = `
                <img src="${imageUrl}" alt="item.name" />
                <div class="cart-item-details">
                    <div class="cart-item-desc">
                    <h3>${item.name}</h3>
                    <p>${item.description}</p>
                    <p id="item-price">Price: &#8377;${item.price}</p>
                    <button onclick="removeFromCart('${item.cartItemId}')">Remove</button>
                    </div>
                    <div class="button-group">
                        <button onclick="decrementQuantity('${item.cartItemId}')">-</button>
                        <p>${item.quantity}</p>
                        <button onclick="incrementQuantity('${item.cartItemId}')">+</button>
                    </div>
                </div>
            `;

            cartContainer.appendChild(productDiv);
            totalPrice += item.price * item.quantity;
        });

        const totalDiv = document.getElementById("total-price");
        totalDiv.innerHTML = `&#8377;${totalPrice.toFixed(2)}`;
    } catch (error) {
        console.error("Error loading cart:", error);
        alert("An error occurred while loading the cart.");
    }
}

document.addEventListener("DOMContentLoaded", function() {
    loadCart(); 
});

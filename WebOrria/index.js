const API_URL = "https://fakestoreapi.com/products";
const carousel = document.getElementById('carousel');
const prevBtn = document.querySelector('.carousel-btn.prev');
const nextBtn = document.querySelector('.carousel-btn.next');

let index = 0;
let productos = [];

async function cargarProductos() {
    try {
        const res = await fetch(API_URL);
        productos = await res.json();
        mostrarProductos();
    } catch(e) {
        console.error("No se pudieron cargar los productos", e);
        carousel.innerHTML = "<p>Errore bat gertatu da produktuak kargatzerakoan.</p>";
    }
}

function mostrarProductos() {
    carousel.innerHTML = '';
    productos.forEach(p => {
        const div = document.createElement('div');
        div.className = 'carousel-item';
        div.innerHTML = `
            <img src="${p.image}" alt="${p.title}">
            <h4>${p.title}</h4>
            <p>${p.price.toFixed(2)}€</p>
        `;
        carousel.appendChild(div);
    });
}

function showSlide(i) {
    const itemWidth = document.querySelector('.carousel-item').offsetWidth + 20;
    carousel.style.transform = `translateX(${-i * itemWidth}px)`;
}

nextBtn.addEventListener('click', () => {
    const totalItems = productos.length;
    const visibleItems = Math.floor(carousel.parentElement.offsetWidth / (250 + 20));
    if(index < totalItems - visibleItems) {
        index++;
        showSlide(index);
    }
});

prevBtn.addEventListener('click', () => {
    if(index > 0) {
        index--;
        showSlide(index);
    }
});

document.addEventListener('DOMContentLoaded', cargarProductos);

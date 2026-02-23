// Aumentar cantidad
function GehituKantitatea(id) {
    let saskia = getSaskia();
    let p = saskia.find(p => p.id === Number(id));
    if (p) {
        p.kantitatea++;
        saveSaskia(saskia);
        renderSaskia();
    }
}

// Disminuir cantidad
function KenduKantitatea(id) {
    let saskia = getSaskia();
    let p = saskia.find(p => p.id === Number(id));
    if (p) {
        p.kantitatea--;
        if (p.kantitatea <= 0) {
            saskia = saskia.filter(x => x.id !== Number(id));
        }
        saveSaskia(saskia);
        renderSaskia();
    }
}

// Eliminar producto completamente
function Ezabatu(id) {
    let saskia = getSaskia().filter(p => p.id !== Number(id));
    saveSaskia(saskia);
    renderSaskia();
}

// Mostrar cesta en HTML
function renderSaskia() {
    let saskia = getSaskia();
    let tbody = document.getElementById("saskia");
    let total = 0;

    tbody.innerHTML = "";

    saskia.forEach(p => {
        let subtotal = p.price * p.kantitatea;
        total += subtotal;

        tbody.innerHTML += `
            <tr>
                <td>${p.title}</td>
                <td>${p.price.toFixed(2)}€</td>
                <td>
                    <button onclick="KenduKantitatea(${p.id})">-</button>
                    ${p.kantitatea}
                    <button onclick="GehituKantitatea(${p.id})">+</button>
                </td>
                <td>${subtotal.toFixed(2)}€</td>
                <td>
                    <button onclick="Ezabatu(${p.id})">X</button>
                </td>
            </tr>
        `;
    });

    document.getElementById("total").innerText = total.toFixed(2) + "€";
}

// Renderizar al cargar la página
document.addEventListener("DOMContentLoaded", () => {
    renderSaskia();
});

function getSaskia() {
    return JSON.parse(localStorage.getItem("saskia")) || [];
}

function saveSaskia(cesta) {
    localStorage.setItem("saskia", JSON.stringify(cesta));
}

function SaskiraGehitu(id, title, price) {
    let saskia = getSaskia();
    let existitu = saskia.find(p => p.id === Number(id));

    if (existitu) {
        existitu.kantitatea++;
    } else {
        saskia.push({
            id: Number(id),
            title: title,
            price: price,
            kantitatea: 1
        });
    }

    saveSaskia(saskia);
    renderSaskia();
}

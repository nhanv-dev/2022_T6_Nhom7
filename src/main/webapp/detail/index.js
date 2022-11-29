let list = []
const naturalKey = location.pathname.split('/')[location.pathname.split('/').length - 1];
axios.get(`${path}/api/commodity/${naturalKey}`, {
    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
}).then(response => {
    list = response.data;
    if (list.length > 0) {
        generateChart()
        window.document.title = list[0].name || "Commodity"
    }
})

const generateChart = () => {
    const canvas = document.getElementById('myChart');
    const dateOptions = {month: "2-digit", day: "2-digit"}
    const data = {
        labels: list.map(item => new Date(item.createdDate).toLocaleDateString("vi-VN", dateOptions)),
        datasets: [{
            label: list[0]?.name + ` (${list[0].unit})` || "Unknown Commodity",
            data: list.map(item => item.price),
            fill: true,
            borderColor: 'rgb(75, 192, 192)',
        }]
    };
    const config = {type: 'line', data: data,};
    new Chart(canvas, config);
}
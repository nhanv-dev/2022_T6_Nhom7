const container = document.querySelector('.data-body');
axios.get("http://localhost:8080/data_warehouse_war_exploded/api/unexpired-data")
    .then(response => {
        const {data} = response
        // const array = []
        // data.forEach((value) => {
        //     const {category} = value
        //     let item = array.filter(item => item?.category === category)
        //     if (item) {
        //         item.commodity = [...item?.commodity]
        //         item?.commodity.push(value)
        //     } else {
        //         item.category = category
        //         item.commodity = [value]
        //     }
        // });
        // console.log(array)
        container.innerHTML = data.map((value, index) => {
            const {category, createdDate, expiredDate, name, naturalKey, percent, price, unit} = value
            return `
                    <div class="data-row">
                        <div class="data-index">${index + 1}</div>
                        <div class="data-cate">${category}</div>
                        <a class="data-name" href="http://localhost:8080/data_warehouse_war_exploded/commodity/${naturalKey}">
                            ${name}
                            <span class="data-unit">(${unit})</span>
                        </a>
                        <div class="data-price">${price}</div>
                        <div class="data-percent ${percent >= 0 ? "data-percent__increase" : "data-percent__decease"}">
                            ${percent / 100}%
                        </div>
                        <div class="data-created">${createdDate}</div>
                    </div>
                `
        }).join('')
    })

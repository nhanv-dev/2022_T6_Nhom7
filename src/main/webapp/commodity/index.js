const container = document.querySelector('.data-table')
const categoryFilter = document.querySelector('.data-filter-category')
const dateOptions = {year: "numeric", month: "2-digit", day: "2-digit"}
const list = []
const state = {
    filters: {
        categories: []
    },
    sorts: [{category: 'all', sortBy: 'name', ascending: true}]
}

axios.get(`${path}/api/unexpired-data`, {
    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
}).then(response => {
    response.data.forEach(item => {
        const contain = list.filter(obj => obj?.category === item.category);
        if (!contain || contain.length === 0) list.push({category: item.category, items: [item]})
        else list.forEach(obj => obj.category === item.category && obj.items.push(item))
    })
    list.sort((a, b) => a.category < b.category ? -1 : 1)
    categoryFilter.innerHTML = generateCategoryFilter(list)
    container.innerHTML = filterAndSort(list).map((obj, index) => generateContainer(obj.category, obj.items, index)).join('')
})


const filterAndSort = (list) => {
    let filterList = list.map(obj => {
        return {category: obj.category, items: [...obj.items]}
    })

    if (state.filters.categories.length > 0) {
        filterList = filterList.filter(obj => {
            let contain = false
            state.filters.categories.forEach(category => {
                if (obj.category === category) contain = true
            })
            return contain
        })
    }
    if (state.search) {
        filterList.forEach(obj => {
            obj.items = obj.items.filter(item => {
                return item.name.toLowerCase().includes(state.search.toLowerCase())
            })
        })
        filterList = filterList.filter(obj => obj.items.length > 0)
    }
    state.sorts.forEach(sort => {
        filterList.forEach(obj => {
            if (sort.category === obj.category || sort.category === 'all') {
                return obj.items.sort((a, b) => {
                    return a[sort.sortBy] < b[sort.sortBy] ? (sort.ascending ? -1 : 1) : (sort.ascending ? 1 : -1)
                })
            }
        })
    })

    return filterList
}

const searchForm = document.querySelector('.data-search')
const searchInput = document.querySelector('.data-search input')

searchInput.addEventListener('change', (e) => {
    state.search = e.target.value
    if (!e.target.value) {
        container.innerHTML = filterAndSort(list).map((obj, index) => {
            const {category, items} = obj
            return generateContainer(category, items, index)
        }).join('')
    }
})

searchForm.addEventListener('submit', (e) => {
    e.preventDefault()
    console.log('submit');
    state.search = searchInput.value
    container.innerHTML = filterAndSort(list).map((obj, index) => {
        const {category, items} = obj
        return generateContainer(category, items, index)
    }).join('')
})

const handleSort = (element) => {
    const {category, sortBy} = element.dataset
    const container = element.closest('.data-container')
    const table = container.querySelector('#data-body-' + category)
    const sortIcons = container.querySelectorAll('.button-sort__icon i')
    const sortUpIcon = element.querySelector('.button-sort__icon .fa-sort-up')
    const sortDownIcon = element.querySelector('.button-sort__icon .fa-sort-down')
    const ascending = sortUpIcon.classList.contains('show')
    sortIcons.forEach(icon => {
        if (icon !== sortDownIcon && icon !== sortUpIcon) icon.classList.remove('show')
    })
    if (ascending) {
        sortUpIcon.classList.remove('show')
        sortDownIcon.classList.add('show')
    } else {
        sortDownIcon.classList.remove('show')
        sortUpIcon.classList.add('show')
    }
    const sort = {category: category, sortBy: sortBy, ascending: ascending}
    state.sorts.forEach((sortState, index) => {
        if (sortState.category === category) {
            state.sorts[index] = sort
            return
        }
        if (index === state.sorts.length - 1) state.sorts.push(sort)
    })
    filterAndSort(list).forEach((obj) => {
        if (obj.category === category)
            table.innerHTML = updateTable(obj.category, obj.items)
    })
}

const handleCategoryFilter = (element) => {
    const inputAll = document.getElementById('check-category-all')
    if (element.value === 'all') {
        if (element.checked) {
            state.filters.categories = []
            const checkboxs = document.querySelectorAll('.form-filter-check-category input')
            checkboxs.forEach(checkbox => checkbox.checked = false)
            inputAll.checked = true
        } else {
            inputAll.checked = true
        }
    } else {
        if (element.checked) {
            state.filters.categories.push(element.value)
            inputAll.checked = false
        } else {
            const categories = state.filters.categories.filter(category => category !== element.value)
            if (categories.length === 0) inputAll.checked = true
            state.filters.categories = categories
        }
    }
    container.innerHTML = filterAndSort(list).map((obj, index) => {
        const {category, items} = obj
        return generateContainer(category, items, index)
    }).join('')
}

const toggleButton = (element) => {
    const plus = element.querySelector('i:nth-child(1)')
    const minus = element.querySelector('i:nth-child(2)')
    if (plus.classList.contains('on')) {
        plus.classList.remove('on')
        plus.classList.add('off')
        minus.classList.remove('off')
        minus.classList.add('on')
    } else {
        plus.classList.remove('off')
        plus.classList.add('on')
        minus.classList.add('off')
        minus.classList.remove('on')
    }
}

const generateCategoryFilter = (list) => {
    const html = list.map((obj, index) => {
        return `
            <div class="form-filter-check-category">
                <input class="form-check-input" type="checkbox" value="${obj.category}" 
                    id="${'check-category-' + index}" onchange="handleCategoryFilter(this)">
                <label class="form-check-label" for="${'check-category-' + index}">
                    ${obj.category}
                </label>
            </div>
        `
    }).join('')

    return `
        <div class="data-options px-0 ">
            <span class="data-category">Category</span>
            <div class="data-options__buttons">
            <button type="button" data-bs-toggle="collapse" onclick="toggleButton(this)" 
                data-bs-target="#accordion-filter-category" aria-expanded="true" aria-controls="accordion-filter-category">
                <i class="fa-solid fa-plus off"></i>
                <i class="fa-solid fa-minus on"></i>
            </button>
            </div>
        </div>
        <div id="accordion-filter-category" class="accordion-collapse collapse show mt-2" aria-labelledby="accordionHeading1" data-bs-parent="#accordion1">
            <div class="accordion-body">
                <div class="form-filter-check-category">
                    <input class="form-check-input" type="checkbox" value="all" 
                        id="check-category-all" onchange="handleCategoryFilter(this)" checked>
                    <label class="form-check-label" for="check-category-all">All</label>
                </div>
                ${html}
            </div>
        </div>
    `
}

const generateContainer = (category, items, index) => {
    const html = items.map((item, index) => {
        const {createdDate, name, naturalKey, percent, price, unit} = item
        return `
            <div class="data-row">
                <div class="data-index">${index + 1}</div>
                <a class="data-name" href="${path}/commodity/${naturalKey}">
                    ${name} <span class="data-unit">(${unit})</span>
                </a>
                <div class="data-price">${price.toLocaleString()}</div>
                <div class="data-percent ${percent >= 0 ? "data-percent__increase" : "data-percent__decease"}">
                    ${percent}%
                </div>
                <div class="data-created">${new Date(createdDate).toLocaleDateString("vi-VN", dateOptions)}</div>
            </div>
            `
    }).join('')

    return `
        <div class="data-container">
            <div class="data-options">
                <span class="data-category">${category}</span>
                <div class="data-options__buttons">
                    <button type="button" data-bs-toggle="collapse" onclick="toggleButton(this)" 
                        data-bs-target="#${'table-category' + index}" aria-expanded="true" aria-controls="${'table-category' + index}">
                        <i class="fa-solid fa-minus on"></i>
                        <i class="fa-solid fa-plus off"></i>
                    </button>
                </div>
            </div>
            <div id="${'table-category' + index}" class="accordion-collapse collapse show" aria-labelledby="accordionHeading1" data-bs-parent="#accordion1">
                <div class="accordion-body">
                    <div class="data-header">
                        <div class="data-header__index">#</div>
                        <div class="data-header__name">
                            <button class="button-sort" data-category="${category}" data-sort-by="name" onclick="handleSort(this)">
                                <span>Name</span>
                                <span class="position-relative button-sort__icon">
                                    <i class="fa-solid fa-sort"></i>
                                    <i class="fa-solid fa-sort-up"></i>
                                    <i class="fa-solid fa-sort-down"></i>
                                </span>
                            </button>
                        </div>
                        <div class="data-header__price">
                            <button class="button-sort" data-category="${category}" data-sort-by="price" onclick="handleSort(this)">
                                <span>Price</span>
                                <span class="position-relative button-sort__icon">
                                    <i class="fa-solid fa-sort"></i>
                                    <i class="fa-solid fa-sort-up"></i>
                                    <i class="fa-solid fa-sort-down"></i>
                                </span>
                            </button>
                        </div>
                        <div class="data-header__percent">
                            <button class="button-sort" data-category="${category}" data-sort-by="percent" onclick="handleSort(this)">
                                <span>Percent</span>
                                <span class="position-relative button-sort__icon">
                                    <i class="fa-solid fa-sort"></i>
                                    <i class="fa-solid fa-sort-up"></i>
                                    <i class="fa-solid fa-sort-down"></i>
                                </span>
                            </button>
                        </div>
                        <div class="data-header__created">Updated date</div>
                    </div>
                    <div class="data-body" id="${'data-body-' + category}">
                        ${html}
                    </div>
                </div>
            </div>
        </div>
        `
}


const updateTable = (category, items) => {
    const html = items.map((item, index) => {
        const {createdDate, name, naturalKey, percent, price, unit} = item
        return `
            <div class="data-row">
                <div class="data-index">${index + 1}</div>
                <a class="data-name" href="${path}/commodity/${naturalKey}">
                    ${name} <span class="data-unit">(${unit})</span>
                </a>
                <div class="data-price">${price.toLocaleString()}</div>
                <div class="data-percent ${percent >= 0 ? "data-percent__increase" : "data-percent__decease"}">
                    ${percent}%
                </div>
                <div class="data-created">${new Date(createdDate).toLocaleDateString("vi-VN", dateOptions)}</div>
            </div>
            `
    }).join('')

    return `
        <div class="data-body" id="${'data-body-' + category}">
            ${html}
        </div>
    `
}
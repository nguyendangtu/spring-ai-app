const stockForm = document.getElementById('stock-form');
const stockTableBody = document.getElementById('stock-table-body');
const searchInput = document.getElementById('searchInput');
const cancelEditBtn = document.getElementById('cancel-edit');
const formTitle = document.getElementById('form-title');

const state = {
    stocks: [],
    editingId: null,
    page: 0,
    size: 10,
    sortBy: 'id',
    direction: 'asc',
    totalPages: 1,
    user: null
};

async function loadCurrentUser() {
    try {
        const res = await fetch('/api/me', { credentials: 'same-origin' });
        if (!res.ok) {
            throw new Error('Not authenticated');
        }
        state.user = await res.json();
        const header = document.querySelector('header');
        if (header) {
            const userBadge = document.createElement('div');
            userBadge.className = 'user-badge';
            userBadge.textContent = `Logged in as: ${state.user.username}`;
            header.appendChild(userBadge);
        }
    } catch (error) {
        window.location.href = '/login.html';
    }
}

async function fetchStocks() {
    const params = new URLSearchParams({
        page: state.page,
        size: state.size,
        sortBy: state.sortBy,
        direction: state.direction
    });

    const searchText = searchInput?.value.trim();
    if (searchText) {
        params.set('search', searchText);
    }

    const res = await fetch(`/api/stocks?${params.toString()}`, { credentials: 'same-origin' });
    if (res.status === 401 || res.status === 403) {
        window.location.href = '/login.html';
        return;
    }
    if (!res.ok) {
        throw new Error('Failed to load stocks');
    }

    const data = await res.json();
    state.stocks = data.content || [];
    state.totalPages = data.totalPages || 1;
    renderStocks();
}

async function fetchStockStats() {
    const res = await fetch('/api/stocks/stats', { credentials: 'same-origin' });
    if (res.status === 401 || res.status === 403) {
        window.location.href = '/login.html';
        return;
    }
    if (!res.ok) {
        throw new Error('Failed to load stock statistics');
    }

    const stats = await res.json();
    document.getElementById('totalStocks').textContent = stats.totalStocks ?? 0;
    document.getElementById('totalQuantity').textContent = stats.totalQuantity ?? 0;
    document.getElementById('totalValue').textContent = formatCurrency(stats.totalValue ?? 0);
    document.getElementById('averagePrice').textContent = formatCurrency(stats.averagePrice ?? 0);
    document.getElementById('highestPrice').textContent = formatCurrency(stats.highestPrice ?? 0);
    document.getElementById('lowestPrice').textContent = formatCurrency(stats.lowestPrice ?? 0);
    document.getElementById('topStock').textContent = stats.topStock ?? 'N/A';
}

function formatCurrency(value) {
    return `$${Number(value).toFixed(2)}`;
}

function renderStocks() {
    stockTableBody.innerHTML = state.stocks.map(stock => `
        <tr>
            <td>${stock.id}</td>
            <td>${stock.symbol}</td>
            <td>${stock.companyName}</td>
            <td>$${Number(stock.price).toFixed(2)}</td>
            <td>${stock.quantity}</td>
            <td>
                <div class="action-buttons">
                    <button class="edit-btn" data-id="${stock.id}">Edit</button>
                    <button class="delete-btn" data-id="${stock.id}">Delete</button>
                </div>
            </td>
        </tr>
    `).join('');

    bindTableActions();
    renderPagination();
}

function renderPagination() {
    const pagination = document.getElementById('pagination');
    if (!pagination) {
        const container = document.createElement('div');
        container.id = 'pagination';
        container.className = 'pagination';
        stockTableBody.parentElement.parentElement.appendChild(container);
    }

    const pageContainer = document.getElementById('pagination');
    pageContainer.innerHTML = `
        <button ${state.page === 0 ? 'disabled' : ''} data-page="prev">Prev</button>
        <span>Page ${state.page + 1} / ${state.totalPages}</span>
        <button ${state.page >= state.totalPages - 1 ? 'disabled' : ''} data-page="next">Next</button>
    `;

    pageContainer.querySelector('[data-page="prev"]').onclick = () => {
        if (state.page > 0) {
            state.page--;
            fetchStocks();
        }
    };

    pageContainer.querySelector('[data-page="next"]').onclick = () => {
        if (state.page < state.totalPages - 1) {
            state.page++;
            fetchStocks();
        }
    };
}

async function submitStock(event) {
    event.preventDefault();

    const payload = {
        symbol: document.getElementById('symbol').value.trim(),
        companyName: document.getElementById('companyName').value.trim(),
        price: Number(document.getElementById('price').value),
        quantity: Number(document.getElementById('quantity').value)
    };

    const isEdit = !!state.editingId;
    const url = isEdit ? `/api/stocks/${state.editingId}` : '/api/stocks';
    const method = isEdit ? 'PUT' : 'POST';

    const res = await fetch(url, {
        method,
        credentials: 'same-origin',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });

    if (res.status === 401 || res.status === 403) {
        window.location.href = '/login.html';
        return;
    }

    if (!res.ok) {
        const errorText = await res.text();
        alert(`Request failed: ${errorText}`);
        return;
    }

    stockForm.reset();
    state.editingId = null;
    cancelEditBtn.classList.add('hidden');
    formTitle.textContent = 'Add Stock';
    state.page = 0;
    await fetchStocks();
    await fetchStockStats();
}

function bindTableActions() {
    document.querySelectorAll('.edit-btn').forEach(button => {
        button.addEventListener('click', async () => {
            const id = button.dataset.id;
            const stock = state.stocks.find(item => String(item.id) === String(id));
            if (!stock) return;

            state.editingId = stock.id;
            formTitle.textContent = 'Edit Stock';
            cancelEditBtn.classList.remove('hidden');

            document.getElementById('stock-id').value = stock.id;
            document.getElementById('symbol').value = stock.symbol;
            document.getElementById('companyName').value = stock.companyName;
            document.getElementById('price').value = stock.price;
            document.getElementById('quantity').value = stock.quantity;
            window.scrollTo({ top: 0, behavior: 'smooth' });
        });
    });

    document.querySelectorAll('.delete-btn').forEach(button => {
        button.addEventListener('click', async () => {
            const id = button.dataset.id;
            if (!confirm('Delete this stock?')) return;

            const res = await fetch(`/api/stocks/${id}`, {
                method: 'DELETE',
                credentials: 'same-origin'
            });
            if (res.status === 401 || res.status === 403) {
                window.location.href = '/login.html';
                return;
            }
            if (!res.ok) {
                alert('Failed to delete stock');
                return;
            }

            if (Number(state.editingId) === Number(id)) {
                resetForm();
            }

            state.page = 0;
            await fetchStocks();
            await fetchStockStats();
        });
    });
}

function resetForm() {
    stockForm.reset();
    state.editingId = null;
    cancelEditBtn.classList.add('hidden');
    formTitle.textContent = 'Add Stock';
}

async function askAiQuestion() {
    const question = document.getElementById('aiQuestion').value.trim();
    const answerBox = document.getElementById('aiAnswer');

    if (!question) {
        answerBox.textContent = 'Please type a question first.';
        return;
    }

    answerBox.textContent = 'Thinking...';

    try {
        const response = await fetch('/api/ai/chat', {
            method: 'POST',
            credentials: 'same-origin',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ question })
        });

        if (response.status === 401 || response.status === 403) {
            window.location.href = '/login.html';
            return;
        }

        if (!response.ok) {
            throw new Error('AI request failed');
        }

        const data = await response.json();
        answerBox.textContent = data.answer || 'No answer returned.';
    } catch (error) {
        console.error(error);
        answerBox.textContent = 'The AI request failed. Check that OPENAI_API_KEY is configured and the app is running.';
    }
}

stockForm.addEventListener('submit', submitStock);
cancelEditBtn.addEventListener('click', resetForm);
searchInput.addEventListener('input', () => {
    state.page = 0;
    fetchStocks();
});
document.getElementById('askAiBtn')?.addEventListener('click', askAiQuestion);

document.addEventListener('DOMContentLoaded', async () => {
    try {
        await loadCurrentUser();
        await fetchStocks();
        await fetchStockStats();
    } catch (error) {
        console.error(error);
        stockTableBody.innerHTML = '<tr><td colspan="6">Unable to load stocks</td></tr>';
    }
});

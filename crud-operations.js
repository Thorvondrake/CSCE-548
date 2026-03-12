// ===================================================================
// CREATE & UPDATE OPERATIONS FOR ALL ENTITIES - NOCTURNE ARCHIVE
// ===================================================================

// AUTHORS CREATE & UPDATE
async function createAuthor() {
    const name = document.getElementById('newAuthorName').value.trim();
    const nationality = document.getElementById('newAuthorNationality').value.trim();
    const birthYear = parseInt(document.getElementById('newAuthorBirthYear').value);
    
    if (!name || !nationality || !birthYear) {
        showStatus('Please fill in all fields for the new author', 'error');
        return;
    }
    
    showStatus('Creating new author...', 'info');
    
    try {
        const response = await fetch(`${BASE_URL}/authors`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ fullName: name, nationality: nationality, birthYear: birthYear })
        });
        
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        
        showStatus(`Author "${name}" created successfully!`, 'success');
        document.getElementById('newAuthorName').value = '';
        document.getElementById('newAuthorNationality').value = '';
        document.getElementById('newAuthorBirthYear').value = '';
        fetchAuthors('all');
        
    } catch (error) {
        console.error('Create author error:', error);
        showStatus(`Failed to create author: ${error.message}`, 'error');
    }
}

async function updateAuthor() {
    const id = parseInt(document.getElementById('updateAuthorId').value);
    const name = document.getElementById('updateAuthorName').value.trim();
    const nationality = document.getElementById('updateAuthorNationality').value.trim();
    const birthYear = parseInt(document.getElementById('updateAuthorBirthYear').value);
    
    if (!id || !name || !nationality || !birthYear) {
        showStatus('Please fill in all fields for the author update', 'error');
        return;
    }
    
    showStatus(`Updating author ID ${id}...`, 'info');
    
    try {
        const response = await fetch(`${BASE_URL}/authors/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ fullName: name, nationality: nationality, birthYear: birthYear })
        });
        
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        
        showStatus(`Author ID ${id} updated successfully!`, 'success');
        document.getElementById('updateAuthorId').value = '';
        document.getElementById('updateAuthorName').value = '';
        document.getElementById('updateAuthorNationality').value = '';
        document.getElementById('updateAuthorBirthYear').value = '';
        fetchAuthors('all');
        
    } catch (error) {
        console.error('Update author error:', error);
        showStatus(`Failed to update author: ${error.message}`, 'error');
    }
}

// TITLES CREATE & UPDATE
async function createTitle() {
    const authorId = parseInt(document.getElementById('newTitleAuthorId').value);
    const titleName = document.getElementById('newTitleName').value.trim();
    const genre = document.getElementById('newTitleGenre').value.trim();
    const year = parseInt(document.getElementById('newTitleYear').value);
    
    if (!authorId || !titleName || !genre || !year) {
        showStatus('Please fill in all fields for the new title', 'error');
        return;
    }
    
    showStatus('Creating new title...', 'info');
    
    try {
        const response = await fetch(`${BASE_URL}/titles`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ authorId: authorId, titleName: titleName, genre: genre, originalPublicationYear: year })
        });
        
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        
        showStatus(`Title "${titleName}" created successfully!`, 'success');
        document.getElementById('newTitleAuthorId').value = '';
        document.getElementById('newTitleName').value = '';
        document.getElementById('newTitleGenre').value = '';
        document.getElementById('newTitleYear').value = '';
        fetchTitles('all');
        
    } catch (error) {
        console.error('Create title error:', error);
        showStatus(`Failed to create title: ${error.message}`, 'error');
    }
}

async function updateTitle() {
    const id = parseInt(document.getElementById('updateTitleId').value);
    const authorId = parseInt(document.getElementById('updateTitleAuthorId').value);
    const titleName = document.getElementById('updateTitleName').value.trim();
    const genre = document.getElementById('updateTitleGenre').value.trim();
    const year = parseInt(document.getElementById('updateTitleYear').value);
    
    if (!id || !authorId || !titleName || !genre || !year) {
        showStatus('Please fill in all fields for the title update', 'error');
        return;
    }
    
    showStatus(`Updating title ID ${id}...`, 'info');
    
    try {
        const response = await fetch(`${BASE_URL}/titles/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ authorId: authorId, titleName: titleName, genre: genre, originalPublicationYear: year })
        });
        
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        
        showStatus(`Title ID ${id} updated successfully!`, 'success');
        document.getElementById('updateTitleId').value = '';
        document.getElementById('updateTitleAuthorId').value = '';
        document.getElementById('updateTitleName').value = '';
        document.getElementById('updateTitleGenre').value = '';
        document.getElementById('updateTitleYear').value = '';
        fetchTitles('all');
        
    } catch (error) {
        console.error('Update title error:', error);
        showStatus(`Failed to update title: ${error.message}`, 'error');
    }
}

// INVENTORY CREATE & UPDATE
async function createBookItem() {
    const titleId = parseInt(document.getElementById('newBookTitleId').value);
    const condition = document.getElementById('newBookCondition').value;
    const price = parseFloat(document.getElementById('newBookPrice').value);
    const signed = document.getElementById('newBookSigned').value === 'true';
    
    if (!titleId || !condition || !price || price <= 0) {
        showStatus('Please fill in all fields with valid values', 'error');
        return;
    }
    
    showStatus('Creating new inventory item...', 'info');
    
    try {
        const response = await fetch(`${BASE_URL}/inventory`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ titleId: titleId, condition: condition, price: price, signed: signed })
        });
        
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        
        showStatus(`Inventory item created successfully! Price: $${price.toFixed(2)}`, 'success');
        document.getElementById('newBookTitleId').value = '';
        document.getElementById('newBookCondition').value = '';
        document.getElementById('newBookPrice').value = '';
        document.getElementById('newBookSigned').value = 'false';
        fetchInventory('all');
        
    } catch (error) {
        console.error('Create book error:', error);
        showStatus(`Failed to create inventory item: ${error.message}`, 'error');
    }
}

async function updateBookItem() {
    const id = parseInt(document.getElementById('updateBookId').value);
    const price = parseFloat(document.getElementById('updateBookPrice').value);
    
    if (!id || !price || price <= 0) {
        showStatus('Please provide valid Item ID and Price', 'error');
        return;
    }
    
    showStatus(`Updating item ID ${id} price...`, 'info');
    
    try {
        const response = await fetch(`${BASE_URL}/inventory/${id}/price`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ price: price })
        });
        
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        
        showStatus(`Item ID ${id} price updated to $${price.toFixed(2)}`, 'success');
        document.getElementById('updateBookId').value = '';
        document.getElementById('updateBookPrice').value = '';
        fetchInventory('all');
        
    } catch (error) {
        console.error('Update book error:', error);
        showStatus(`Failed to update item price: ${error.message}`, 'error');
    }
}

// SALES CREATE & UPDATE
async function createSale() {
    const itemId = parseInt(document.getElementById('newSaleItemId').value);
    const finalPrice = parseFloat(document.getElementById('newSaleFinalPrice').value);
    const buyerName = document.getElementById('newSaleBuyerName').value.trim();
    
    if (!itemId || !finalPrice || finalPrice <= 0) {
        showStatus('Please provide valid Item ID and Sale Price', 'error');
        return;
    }
    
    showStatus('Recording new sale...', 'info');
    
    try {
        const response = await fetch(`${BASE_URL}/sales`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ itemId: itemId, finalPrice: finalPrice, buyerName: buyerName || null })
        });
        
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        
        showStatus(`Sale recorded successfully! Amount: $${finalPrice.toFixed(2)}`, 'success');
        document.getElementById('newSaleItemId').value = '';
        document.getElementById('newSaleFinalPrice').value = '';
        document.getElementById('newSaleBuyerName').value = '';
        fetchSales('all');
        
    } catch (error) {
        console.error('Create sale error:', error);
        showStatus(`Failed to record sale: ${error.message}`, 'error');
    }
}

async function updateSale() {
    const id = parseInt(document.getElementById('updateSaleId').value);
    const itemId = parseInt(document.getElementById('updateSaleItemId').value);
    const finalPrice = parseFloat(document.getElementById('updateSaleFinalPrice').value);
    const buyerName = document.getElementById('updateSaleBuyerName').value.trim();
    
    if (!id || !itemId || !finalPrice || finalPrice <= 0) {
        showStatus('Please fill in all fields with valid values', 'error');
        return;
    }
    
    showStatus(`Updating sale ID ${id}...`, 'info');
    
    try {
        const currentDate = new Date().toISOString();
        const response = await fetch(`${BASE_URL}/sales/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ 
                itemId: itemId, 
                saleDate: currentDate, 
                finalPrice: finalPrice, 
                buyerName: buyerName || null 
            })
        });
        
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        
        showStatus(`Sale ID ${id} updated successfully!`, 'success');
        document.getElementById('updateSaleId').value = '';
        document.getElementById('updateSaleItemId').value = '';
        document.getElementById('updateSaleFinalPrice').value = '';
        document.getElementById('updateSaleBuyerName').value = '';
        fetchSales('all');
        
    } catch (error) {
        console.error('Update sale error:', error);
        showStatus(`Failed to update sale: ${error.message}`, 'error');
    }
}
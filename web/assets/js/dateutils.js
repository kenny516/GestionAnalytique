function formatDateFR(dateStr) {
    const date = new Date(dateStr);

    const formattedDate = new Intl.DateTimeFormat('fr-FR', { 
    weekday: 'long', 
    year: 'numeric', 
    month: 'long', 
    day: 'numeric'
    }).format(date);

    return formattedDate;
}
let chartInstance = null;

function updateChart() {
    const view = document.getElementById('viewSelect').value;
    const endpoint = view === 'year' ? 'by-year' : 'by-region';
    const labelKey = view === 'year' ? 'year' : 'region';
    const title = view === 'year' ? 'Year' : 'Region';
    
    fetch(`http://localhost:5000/api/conflicts/${endpoint}`)
        .then(response => response.json())
        .then(data => {
            const labels = data.map(item => item[labelKey]);
            const counts = data.map(item => item.conflict_count);

            const ctx = document.getElementById('conflictChart').getContext('2d');
            
            // Destroy previous chart instance if it exists
            if (chartInstance) {
                chartInstance.destroy();
            }

            chartInstance = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [{
                        label: `Conflicts by ${title}`,
                        data: counts,
                        borderColor: 'rgba(75, 192, 192, 1)',
                        backgroundColor: 'rgba(75, 192, 192, 0.2)',
                        fill: false,
                        tension: 0.2
                    }]
                },
                options: {
                    responsive: {
                        maintainAspectRatio: true
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            title: {
                                display: true,
                                text: 'Number of Conflicts'
                            }
                        },
                        x: {
                            title: {
                                display: true,
                                text: title
                            }
                        }
                    }
                }
            });
        })
        .catch(error => console.error('Error fetching data:', error));
}

// Initialize chart on page load
document.addEventListener('DOMContentLoaded', updateChart);

document.getElementById('fetchWeatherBtn').addEventListener('click', async () => {
    const city = document.getElementById('cityInput').value;
    const response = await fetch(`/weather?city=${city}`);
    
    if (!response.ok) {
        alert("Error fetching weather data");
        return;
    }

    const weatherData = await response.json();
    
    const weatherTableBody = document.getElementById('weatherTableBody');
    weatherTableBody.innerHTML = `
        <tr>
            <td>${weatherData.weatherCondition}</td>
            <td>${weatherData.temperature}</td>
            <td>${weatherData.feelsLike}</td>
            <td>${weatherData.humidity}</td>
            <td>${weatherData.windSpeed} m/s</td> <!-- Display wind speed in m/s -->
        </tr>
    `;
});

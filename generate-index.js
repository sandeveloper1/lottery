const fs = require('fs');
const path = require('path');

// Get all HTML files except index.html and template files
const files = fs.readdirSync('.')
  .filter(f => f.endsWith('.html') && f !== 'index.html' && !f.includes('template'));

let cards = '';

files.forEach(file => {
  // Derive JSON filename from HTML filename
  const jsonFile = file.replace('.html', '.json');
  if (!fs.existsSync(jsonFile)) return;

  // Read JSON data
  const data = JSON.parse(fs.readFileSync(jsonFile, 'utf8'));
  const code = data.draw_number || data.code || '';
  const name = data.lottery_name || data.name || '';
  const date = data.draw_date || data.date || '';

  cards += `
    <a href="${file}" class="card">
      <div class="code">${code}</div>
      <div class="name">${name}</div>
      <div class="date">${date}</div>
    </a>
  `;
});

// HTML template for the homepage
const html = `
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Lottery Cards</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <style>
    body { font-family: 'Segoe UI', Arial, sans-serif; background: #e9ecf3; margin: 0; }
    .header { background: #1a3c7c; color: #fff; padding: 24px 0; text-align: center; font-size: 2em; }
    .welcome { background: linear-gradient(90deg, #a1c4fd, #c2e9fb); color: #333; text-align: center; padding: 12px 0; font-size: 1.2em; }
    .cards { display: flex; flex-wrap: wrap; gap: 20px; justify-content: center; padding: 30px 0; }
    .card {
      background: #2155cd;
      color: #fff;
      border-radius: 16px;
      width: 220px;
      min-height: 120px;
      box-shadow: 0 4px 16px rgba(0,0,0,0.08);
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      margin: 0 8px 24px 8px;
      position: relative;
      overflow: hidden;
      text-decoration: none;
    }
    .card .code {
      position: absolute;
      top: 12px; right: 12px;
      background: #3b5998;
      color: #fff;
      border-radius: 8px;
      padding: 2px 10px;
      font-size: 0.95em;
      font-weight: bold;
    }
    .card .name {
      margin: 36px 0 0 16px;
      font-size: 1.3em;
      font-weight: bold;
      letter-spacing: 1px;
    }
    .card .date {
      margin: 18px 0 0 16px;
      background: #fff;
      color: #2155cd;
      border-radius: 8px;
      padding: 4px 12px;
      font-size: 1.1em;
      font-weight: bold;
      align-self: flex-start;
    }
    @media (max-width: 700px) {
      .cards { flex-direction: column; align-items: center; }
      .card { width: 90vw; }
    }
  </style>
</head>
<body>
  <div class="header">Ponkudam</div>
  <div class="welcome">welcome</div>
  <div class="cards">
    ${cards}
  </div>
</body>
</html>
`;

fs.writeFileSync('index.html', html);
console.log('index.html generated!');

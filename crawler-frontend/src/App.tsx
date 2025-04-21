import React from 'react';
import './App.css';
import TableManager from './components/TableManager';

function App() {
  return (
    <div className="App">
      <h1 style={{ textAlign: 'center', marginTop: 20 }}>Database Table Viewer</h1>
      <h1 style={{ textAlign: 'center', marginTop: 20 }}>(Crawler)</h1>
      <TableManager />
    </div>
  );
}

export default App;
import React, { useEffect, useState } from 'react';
import axios from 'axios';

export default function TableManager() {
  const [tables, setTables] = useState([]);
  const [selectedTable, setSelectedTable] = useState<any>(null);
  const [search, setSearch] = useState('');
  const [editingRowIndex, setEditingRowIndex] = useState<number | null>(null);
  const [editedRowData, setEditedRowData] = useState<any>({});

  useEffect(() => {
    axios.get('http://127.0.0.1:8080/api/metadata/tables')
      .then(res => setTables(res.data))
      .catch(console.error);
  }, []);

  const handleExport = (tableName: string) => {
    axios({
      url: `http://localhost:8080/api/metadata/export/model/${tableName}`,
      method: 'GET',
      responseType: 'blob',
    }).then(res => {
      const url = window.URL.createObjectURL(new Blob([res.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `${tableName}.zip`);
      document.body.appendChild(link);
      link.click();
    });
  };

  const handleEditClick = (idx: number) => {
    setEditingRowIndex(idx);
    setEditedRowData({ ...selectedTable.data[idx] });
  };

  const handleInputChange = (colName: string, value: string) => {
    setEditedRowData((prev: any) => ({
      ...prev,
      [colName]: value
    }));
  };

  const handleSave = () => {
    const newData = [...selectedTable.data];
    if (editingRowIndex !== null) {
      newData[editingRowIndex] = editedRowData;
      setSelectedTable((prev: any) => ({
        ...prev,
        data: newData,
      }));
    }
    setEditingRowIndex(null);
    setEditedRowData({});
  };

  const handleCancel = () => {
    setEditingRowIndex(null);
    setEditedRowData({});
  };

  const handleDelete = (idx: number) => {
    const deletedRow = selectedTable.data[idx];
    axios.delete(`http://localhost:8080/api/metadata/tables/${selectedTable.table}`, { data: { row: deletedRow } })
      .then(() => {
        // Update the table data after deletion
        const newData = selectedTable.data.filter((_, index) => index !== idx);
        setSelectedTable((prev: any) => ({
          ...prev,
          data: newData,
        }));
        alert('Row deleted successfully');
      })
      .catch((error) => {
        console.error('Error deleting row:', error);
        alert('Failed to delete the row');
      });
  };

  const filteredTables = tables.filter((t: any) =>
    (t?.table || '').toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div style={{ padding: 16 }}>
      <input
        placeholder="Search tables..."
        value={search}
        onChange={e => setSearch(e.target.value)}
        style={{ padding: 8, marginBottom: 16, width: '100%' }}
      />
      <div style={{ display: 'grid', gap: 16, gridTemplateColumns: 'repeat(auto-fill, minmax(250px, 1fr))' }}>
        {filteredTables.map((table: any) => (
          <div
            key={table.table}
            style={{ border: '1px solid #ccc', borderRadius: 8, padding: 12, cursor: 'pointer' }}
            onClick={() => {
              setSelectedTable(table);
              setEditingRowIndex(null);
            }}
          >
            <strong>{table.table}</strong>
            <div style={{ fontSize: 12, color: '#666' }}>{table.columns.length} columns</div>
          </div>
        ))}
      </div>

      {selectedTable && (
        <div style={{ marginTop: 24 }}>
          <h2>{selectedTable.table}</h2>
          <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: 12 }}>
            <thead>
              <tr>
                {selectedTable.columns.map((col: any) => (
                  <th key={col.name} style={{ border: '1px solid #ccc', padding: 8 }}>{col.name}</th>
                ))}
                <th style={{ border: '1px solid #ccc', padding: 8 }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {selectedTable.data.map((row: any, idx: number) => (
                <tr key={idx}>
                  {selectedTable.columns.map((col: any) => (
                    <td key={col.name} style={{ border: '1px solid #ccc', padding: 8 }}>
                      {editingRowIndex === idx ? (
                        <input
                          type="text"
                          value={editedRowData[col.name] || ''}
                          onChange={(e) => handleInputChange(col.name, e.target.value)}
                          style={{ width: '100%' }}
                        />
                      ) : (
                        row[col.name]
                      )}
                    </td>
                  ))}
                  <td style={{ border: '1px solid #ccc', padding: 8 }}>
                    {editingRowIndex === idx ? (
                      <>
                        <button onClick={handleSave} style={{ marginRight: 8 }}>Save</button>
                        <button onClick={handleCancel}>Cancel</button>
                        <button onClick={() => handleDelete(idx)} style={{ marginLeft: 8, backgroundColor: 'red', color: 'white' }}>Delete</button>
                      </>
                    ) : (
                      <button onClick={() => handleEditClick(idx)}>Edit</button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          <button onClick={() => handleExport(selectedTable.table)} style={{ marginTop: 16 }}>
            Export Model
          </button>
        </div>
      )}
    </div>
  );
}

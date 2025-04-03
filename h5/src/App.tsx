import "./App.css";
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import NoteEdit from './pages/NoteEdit';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/note-edit/:noteKey?" element={<NoteEdit />} />
      </Routes>
    </Router>
  );
}

export default App;

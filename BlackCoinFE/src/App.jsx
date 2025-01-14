import axios from 'axios'
import './App.css'
import Navbar from './components/Navbar/Navbar'
import { useEffect, useState } from 'react'

function App() {
  const[blocks, setBlocks] = useState([])

  useEffect(() => {
    axios.get('http://localhost:8080/fe/allblock')
      .then(response => {
        setBlocks(response.data)
      })
  },[0])

  return (
    <div className='min-h-screen bg-slate-700'>
      <Navbar />
      <div>
        <div className="container mx-auto p-3">
          <div className="bg-slate-950 p-3 rounded-lg">
            <div className="grid grid-cols-6 text-slate-200 gap-1">
              <div className="col-span-1 bg-slate-800 p-3 rounded">Block ID</div>
              <div className="col-span-1 bg-slate-800 p-3 rounded">Timestamp</div>
              <div className="col-span-1 bg-slate-800 p-3 rounded">Hash</div>
              <div className="col-span-1 bg-slate-800 p-3 rounded">Previous Hash</div>
              <div className="col-span-1 bg-slate-800 p-3 rounded">Nonce</div>
              <div className="col-span-1 bg-slate-800 p-3 rounded">Transactions</div>
              {blocks && blocks.map(block => (
                <>
                  <div className="col-span-1 bg-slate-900 p-3 rounded">{block.blockId}</div>
                  <div className="col-span-1 bg-slate-900 p-3 rounded">{block.timestamp}</div>
                  <div className="col-span-1 bg-slate-900 p-3 rounded truncate">{block.hash}</div>
                  <div className="col-span-1 bg-slate-900 p-3 rounded truncate">{block.previousHash}</div>
                  <div className="col-span-1 bg-slate-900 p-3 rounded">{block.nonce}</div>
                  <div className="col-span-1 bg-slate-900 p-3 rounded">{block.transactions}</div>
                </>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default App

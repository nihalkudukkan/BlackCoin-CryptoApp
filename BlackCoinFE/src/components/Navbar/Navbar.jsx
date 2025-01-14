import React from 'react'
import { Link } from 'react-router-dom'

export default function Navbar() {
  return (
    <div className="w-full bg-slate-900 p-2 flex justify-between sticky top-0 z-50 text-slate-200 border-b border-slate-300/10">
        <div className="text-2xl">
            <Link to={"/"}>Black Coin</Link>
        </div>
        <ul>
            <li className=''>
              <Link to={"/login"}>Login</Link>
            </li>
        </ul>
    </div>
  )
}

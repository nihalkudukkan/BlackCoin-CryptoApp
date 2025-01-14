import React from 'react'
import Navbar from '../Navbar/Navbar'
import axios from 'axios'
import EC from 'elliptic/lib/elliptic/ec';

export default function Login() {
  const ec = new EC('secp256k1');
  const [keys, setKeys] = React.useState({
    publicKey: '',
    privateKey: ''
  })

  const handleSubmit = (e) => {
    e.preventDefault()
    console.log(keys)
    // axios.post('http://localhost:5000/login', keys)
    try {
      const keyPair = ec.keyFromPrivate(keys.privateKey, 'hex');
      publicKey = keyPair.getPublic('hex');
    } catch (error) {
      
    }
  }

  return (
    <>
    <Navbar />
    <div className='w-full page bg-slate-700 flex items-center justify-center'>
        <form className='bg-slate-800 w-full text-slate-200 p-4 flex flex-col gap-3 sm:w-3/4 md:w-3/5 lg:w-1/3 sm:rounded'
        onSubmit={handleSubmit}>
            <div className="text-center text-lg">Login</div>
            <div className="flex flex-col gap-2 text-slate-900">
                <input className='rounded p-1 bg-slate-600' type="text" name="publickey" id="publickey" placeholder='public key' required
                onChange={(e)=>{setKeys({...keys, publicKey: e.target.value})}} value={keys.publicKey} />
                <input className='rounded p-1 bg-slate-600' type="text" name="privatekey" id="privatekey" placeholder='private key' required
                onChange={(e)=>{setKeys({...keys, privateKey: e.target.value})}} value={keys.privateKey} />
            </div>
            <div className="text-end">
                <input className='border py-2 px-3 rounded border-slate-700 bg-slate-800 hover:bg-slate-700 duration-100' type="submit" value="login" />
            </div>
        </form>
    </div>
    </>
  )
}

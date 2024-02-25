import React from "react";
import Doutores from "./scenes/doctors/Doctors";
import Pacientes from './scenes/patients/Patients';
import Procedimentos from './scenes/procedures/Procedures';
import Employees from './scenes/employees/Employees'; // Importando o componente de funcionários
import { Navbar } from "./scenes/global/navbar/Navbar";
import { Home } from "./scenes/home/Home"
import  Sidebar  from './scenes/global/sidebar/Sidebar';
import {Login} from './scenes/auth/login/Login';
import { Footer } from './scenes/global/footer/Footer';
import { ProtectedRoute } from './components/protectedRoute/ProtectedRoute';
import "./assets/global.scss"
import {
  createBrowserRouter,
  RouterProvider,
  Outlet
} from "react-router-dom";


function App() {
  const Global = () => {
    return (
      <div className="main">
        <Navbar />
        <div className='container'>
          <div className='sidebarContainer'>
            <Sidebar />
          </div>
          <div className='contentContainer'>
            <Outlet />
          </div>
        </div>
        <Footer/>
      </div>
    )
  }

  const router = createBrowserRouter([
    {
      path:"/login",
      element:<Login/>
    },
    {
      path: "/",
      element: <Global />,
      children: [
        {
          path: "",
          element: <ProtectedRoute element={<Home />} />,
        },
        {
          path: "doutores",
          element: <ProtectedRoute element={<Doutores />} />,
        },
        {
          path: "pacientes",
          element: <ProtectedRoute element={<Pacientes />} />,
        },
        {
          path: "procedimentos",
          element: <ProtectedRoute element={<Procedimentos />} />,
        },
        {
          path: "funcionarios",
          element: <ProtectedRoute element={<Employees />} />, 
        },
      ]
    }
  ]);

  return (
    <RouterProvider router={router} />
  )
}

export default App;

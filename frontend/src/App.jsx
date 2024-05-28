import React from "react";
import Doutores from "./scenes/doctors/Doctors";
import Pacientes from './scenes/patients/Patients';
import Procedimentos from './scenes/procedures/Procedures';
import Employees from './scenes/employees/Employees'; 
import Profile from './scenes/account/profile/Profile';
import { Navbar } from "./scenes/global/navbar/Navbar";
import { Home } from "./scenes/home/Home"
import  Sidebar  from './scenes/global/sidebar/Sidebar';
import {Login} from './scenes/auth/login/Login';
import { ForgotPassword } from './scenes/auth/forgotPassword/ForgotPassword'; 
import { ResetPassword } from './scenes/auth/resetPassword/ResetPassword';
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
      path:"/forgot-password",
      element:<ForgotPassword/>
    },
    {
      path: "/reset-password",
      element: <ResetPassword />
    },
    {
      path: "/",
      element: <Global />,
      children: [
        {
          path: "",
          element: <ProtectedRoute element={<Home />} roles={["ROLE_DOUTOR", "ROLE_ATENDENTE", "ROLE_NEGOCIO"]} />,
        },
        {
          path: "doutores",
          element: <ProtectedRoute element={<Doutores />} roles={["ROLE_ATENDENTE", "ROLE_NEGOCIO"]} />,
        },
        {
          path: "pacientes",
          element: <ProtectedRoute element={<Pacientes />} roles={["ROLE_ATENDENTE", "ROLE_NEGOCIO"]} />,
        },
        {
          path: "procedimentos",
          element: <ProtectedRoute element={<Procedimentos />} roles={["ROLE_ATENDENTE", "ROLE_NEGOCIO"]} />,
        },
        {
          path: "funcionarios",
          element: <ProtectedRoute element={<Employees />} roles={["ROLE_ATENDENTE", "ROLE_NEGOCIO"]} />,
        },
        {
          path: "perfil",
          element: <ProtectedRoute element={<Profile />} roles={["ROLE_DOUTOR", "ROLE_ATENDENTE", "ROLE_NEGOCIO"]} />,
        },
      ]
    }
  ]);

  return (
    <RouterProvider router={router} />
  )
}

export default App;
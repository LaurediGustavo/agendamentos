import  Doutores from "./scenes/doctors/Doctors";
import Pacientes from './scenes/patients/Patients';
import Procedimentos from './scenes/procedures/Procedures';
import { Navbar } from "./scenes/global/navbar/Navbar";
import { Home } from "./scenes/home/Home"
import  Sidebar  from './scenes/global/sidebar/Sidebar';
import {Login} from './scenes/auth/login/Login';
import { Footer } from './scenes/global/footer/Footer';
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
      path: "/",
      element: <Global />,
      children: [
        {
          path: "/",
          element: <Home />,
        },
        {
          path: "doutores",
          element: <Doutores />,
        },
        {
          path: "pacientes",
          element: <Pacientes />,
        },
        {
          path: "procedimentos",
          element: <Procedimentos />,
        },
      ]
    },
    {
      path:"/login",
      element:<Login/>
    }
  ]);


  return (
    <RouterProvider router={router} />
  )
}

export default App
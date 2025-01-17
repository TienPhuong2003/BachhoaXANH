// import React from "react";
// import Banner from "../../components/Banner/Banner";
// import BannerBottom from "../../components/Banner/BannerBottom";
// import BestSellers from "../../components/home/BestSellers/BestSellers";
// import NewArrivals from "../../components/home/NewArrivals/NewArrivals";
// import Sale from "../../components/home/Sale/Sale";
// import SpecialOffers from "../../components/home/SpecialOffers/SpecialOffers";
// import YearProduct from "../../components/home/YearProduct/YearProduct";

// const Home = () => {
//   return (
//     <div className="w-full mx-auto">
//       {/* Banner and BannerBottom */}
//       {/* <div className="max-w-screen-xl mx-auto px-4">
//         <Banner />
//         <BannerBottom />
//       </div> */}

//       {/* Main content area with 80% width on the right */}
//       <div className="flex justify-between w-full mx-auto px-4">
//         {/* Sidebar or HeaderBottom (20%) */}
//         <div className="w-1/5 bg-gray-100">
        
//         </div>

//         {/* Main content (80%) */}
//         <div className="w-4/5">
//           <Banner />
//           <BannerBottom />
//           <Sale />
//           <NewArrivals />
//           <BestSellers />
//           <YearProduct />
//           <SpecialOffers />
//         </div>
//       </div>
//     </div>
//   );
// };

// export default Home;
import React from "react";
import { Link, Route, Routes } from "react-router-dom";  // Thêm Route và Routes
import Banner from "../../components/Banner/Banner";
import BannerBottom from "../../components/Banner/BannerBottom";
import BestSellers from "../../components/home/BestSellers/BestSellers";
import NewArrivals from "../../components/home/NewArrivals/NewArrivals";
import Sale from "../../components/home/Sale/Sale";
import SpecialOffers from "../../components/home/SpecialOffers/SpecialOffers";
import YearProduct from "../../components/home/YearProduct/YearProduct";
// import CategoryDetailPage from "../../components/home/CategoryDetail/CategoryDetailPage";  // Import CategoryDetailPage

const Home = () => {
  return (
    <div className="w-full mx-auto">
      <div className="flex justify-between w-full mx-auto px-4">
        <div className="w-1/5 bg-gray-100">
          {/* Sidebar content could go here */}
        </div>

        <div className="w-4/5">
          <Banner />
          <BannerBottom />
          <Sale />

          {/* Routes for category details */}
          {/* <Routes>
            <Route path="/category/:categoryId" element={<CategoryDetailPage />} />
          </Routes> */}

          <div>
            <SpecialOffers />
            <NewArrivals />
            <div className="mt-4 text-center">
              <Link 
                to="/category/new-arrivals"  // Điều hướng đến route category
                className="text-blue-500 underline"
              >
                Xem thêm về các sản phẩm mới
              </Link>
            </div>
          </div>

          <BestSellers />
          <YearProduct />
        </div>
      </div>
    </div>
  );
};

export default Home;

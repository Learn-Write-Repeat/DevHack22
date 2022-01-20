import React from 'react'
import pdf from './pdf.png'
// import {Modal} from 'react-bootstrap'

import '../../styles/file.css'
const File = ({file}) => {
    //href={file.url}

    // const [longString,setLongString]=useState(true);
    const truncate=(string,n)=>{
        return string.length>15? string.substr(0,n-1)+'...': string;
    }


    const getFileSize=(fileSizeInBytes)=>{
        let i=-1;
        const byetUnits = [' kB', ' MB', ' GB', ' TB', 'PB', 'EB', 'ZB', 'YB'];
        do{
            fileSizeInBytes=fileSizeInBytes/1024;
            i++;
        }
        while(fileSizeInBytes>1024);
        return Math.max(fileSizeInBytes,0.1).toFixed(1)+byetUnits[i];
   }
    

  const getImage=()=>{
     if(file.file.substring(file.file.indexOf('.') + 1)==='jpg'||file.file.substring(file.file.indexOf('.') + 1)==='png'||file.file.substring(file.file.indexOf('.') + 1)==='jpeg'){
         return 'https://img.icons8.com/fluency/2x/image-file.png';
     }
     
     if(file.file.substring(file.file.indexOf('.') + 1)==='pdf'){
         return pdf;
     }
     else{
         return "https://img.icons8.com/fluency/2x/edit-text-file.png"
     }
  }

    return (
       <>
        <a href={file.url} target='_blank'  className="file">
           <img src={getImage()} alt="" />
           <p>{truncate(file.file,15)}</p>
            <span style={{fontSize:'12px'}}>{getFileSize(file?.size)}</span>
        </a>
    
        </>
    )
}

export default File

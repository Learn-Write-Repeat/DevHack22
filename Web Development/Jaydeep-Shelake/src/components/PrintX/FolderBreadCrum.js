import React from 'react'
import {Breadcrumb} from 'react-bootstrap'
import { Link } from 'react-router-dom'
import { ROOT_FOLDER } from '../../hooks/useFolder'
const FolderBreadCrum = ({currentFolder}) => {
    //if current floder is root foledr our path is emty otherwise path is going to start from rootfolder
    let path = currentFolder===ROOT_FOLDER?[]:[ROOT_FOLDER]
    if(currentFolder) path=[...path,...currentFolder?.path]

    return (
        <Breadcrumb 
        className="flex-grow-1" 
        listProps={{className:'bg-white p-0 m-0 pl-0'}}
        >
            {path.map((folder,index)=>(
                <Breadcrumb.Item 
                linkAs={Link} 
                linkProps={{
                    to:{
                        pathname:folder?.id?`/folder/${folder?.id}`:'/',
                        state:{folder:{...folder,path:path.slice(1,index)}}
                    }
                }}
                key={index} 
                className="text-truncate d-inline-block" style={{maxWidth:"150px"}}>
                {folder?.name}
                </Breadcrumb.Item>
               )
            )}
            {currentFolder && (
                <Breadcrumb.Item className="text-truncate d-inline-block"  active>
                   {currentFolder.name}
                </Breadcrumb.Item>
            )}
        </Breadcrumb>
    )
}

export default FolderBreadCrum

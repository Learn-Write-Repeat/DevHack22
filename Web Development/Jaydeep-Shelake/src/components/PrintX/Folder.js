import { Button } from 'react-bootstrap'
import React from 'react'
import FolderOpenIcon from '@material-ui/icons/FolderOpen';
import { Link } from 'react-router-dom';
const Folder = ({folder}) => {
    return (
       
        <Button
        to={{
            pathname:`/folder/${folder.id}`,
            state:{folder:folder}
        }} 
        as={Link}
        variant="outline-warning" 
        className="text-truncate w-100">
            <FolderOpenIcon/>
           <p>{folder.name}</p>
        </Button>
    )
}

export default Folder

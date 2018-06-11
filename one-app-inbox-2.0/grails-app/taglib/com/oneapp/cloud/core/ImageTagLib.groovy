
package com.oneapp.cloud.core

class ImageTagLib {


static namespace = 'images'
  def imageService

  def getImageURL_50 = { attrs->
	 if(attrs?.item){
      	out << "<img src='${imageService.getImageURL50(attrs?.item)}' height='50px' width='50px'/>"
       imageService.deleteImage(attrs?.item)
    }

  }
  def getImageURL_300 = { attrs->
	
       if(attrs?.item) {
        def s = "<img src='${imageService.getImageURL300(attrs?.item)}' height='300px' width='300px'/>"
        out << s
        imageService.deleteImage(attrs?.item)
       }
  }
  
  def getImageURL = { attrs->
  
   if(attrs?.item){
      	out << "<img src='${imageService.getImageURL(attrs?.item)}' />"
        //imageService.deleteImage(attrs?.item)
    }
  
  }
  def deleteImage={ attrs->
     imageService.deleteImage(attrs?.item)
  }
}
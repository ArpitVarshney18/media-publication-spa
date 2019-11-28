/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2018 Adobe Systems Incorporated
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~     http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
 import React, { Component } from 'react';
 import { MapTo } from '@adobe/cq-react-editable-components';
 import DOMPurify from 'dompurify';
 import extractModelId from '../../utils/extract-model-id';
 import AssetDetails from './AssetDetails';
 
 require('./AssetGallery.css');
 
 /**
  * Default Edit configuration for the AssetGallery component
  *
  * @type EditConfig
  */
 const AssetGalleryEditConfig = {
 
     emptyLabel: 'Asset Gallery',
 
     isEmpty: function (props) {
         return !props || !props.title || props.title.trim().length < 1;
     }
 };
 
 /**
  * Asset Gallery React component
  */
 class AssetGallery extends Component {
 
     constructor(props) {
         super(props);
         this.state = { currentSelectedObj: {}, showInfo: false };
         this.fetchImageInfo = this.fetchImageInfo.bind(this);
         this.backButtonHandler = this.backButtonHandler.bind(this);
     }
 
     fetchImageInfo(item) {
         this.setState((prevState, props) => ({
             ...prevState,
             currentSelectedObj: item,
             showInfo: true
         }));   
     }
 
     backButtonHandler(val) {
         this.setState((prevState, props) => ({
             ...prevState,
             showInfo: val
         }));
     }
 
     render() {
         const items = this.props.assetDataList.map((item, key) =>
             <div className="col-sm-4 cmp-asset-gallery__card-container">
                 <div className="cmp-image">
                     <a href={item.detailPagePath}>
                         <img
                             onClick={() => this.fetchImageInfo(item)}
                             className="cmp-image_image" src={item.thumnailPath} />
                     </a>
                     <br />
                     <span className="cmp-image__file-name">{item.fileName}</span>
                 </div>
             </div>
         );
         return <div id="cmp-asset-gallery" className="cmp-asset-gallery">
             {!this.state.showInfo && (
                 <div>
                     <h1 className="cmp-asset-gallery__title">{this.props.title}</h1>
                     <div className="container">
                         <div className="row">
                             {items}
                         </div>
                     </div>
                 </div>
             )}
             {this.state.showInfo && (
                 <AssetDetails
                     data={this.state.currentSelectedObj}
                     onClickOfBackButton={val => this.backButtonHandler(val)}
                 />
             )}
         </div>;
     }
 }
 
 export default MapTo('media_publication_spa/components/asset-gallery')(AssetGallery, AssetGalleryEditConfig);
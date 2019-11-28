import React, { Component } from "react";
require('./AssetGalleryDetail.css');

class AssetDetails extends Component {
  constructor(props) {
    super(props);
    this.backButtonHandler = this.backButtonHandler.bind(this);
  }

  backButtonHandler() {
    this.props.onClickOfBackButton(false);
  }

  render() {
    return (
      <div id="cmp-asset-gallery-detail">
        <h1 className="cmp-asset-gallery__title">Asset Gallery Detail</h1>
        <div className="container cmp-asset-gallery-container">
          <div className="row cmp-asset-gallery-row">
            <div className="col-sm-6">
              <table className="table">
                <tbody>
                  <tr>
                    <td>Asset Type</td>
                    <td>{this.props.data.assetType}</td>
                  </tr>
                  <tr>
                    <td>Owner</td>
                    <td>{this.props.data.assetOwnerName}</td>
                  </tr>
                  <tr>
                    <td>Owner's Email</td>
                    <td>{this.props.data.assetOwnerEmail}</td>
                  </tr>
                  <tr>
                    <td>Download Link</td>
                    <td>
                      <a href={this.props.data.assetDownloadLink} download="download">
                        Download
                      </a>
                    </td>
                  </tr>
                </tbody>
              </table>
              <div className="cmp-asset-gallery__back-button">
                <a className="cmp-button btn btn-primary" onClick={this.backButtonHandler}>OK</a>
              </div>
            </div>
            <div className="col-sm-6">
              <img className="cmp-asset-gallery__image" src={this.props.data.assetImage} alt="assest image" />
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default AssetDetails;
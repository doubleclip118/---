// CloudInfoForm.js
import React, { useState } from 'react';

function CloudInfoForm({ cloudProvider, setCloudProvider, fetchExistingCloudInfo, formType }) {
  const [formData, setFormData] = useState({
    driverName: '',
    providerName: '',
    driverLibFileName: '',
    credentialName: '',
    regionName: '',
    regionKey: '',
    regionValue: '',
    zoneKey: '',
    zoneValue: '',
    configName:''
  });

  const [awsSpecificData, setAwsSpecificData] = useState({
    credentialAccessKey: '',
    credentialAccessKeyVal: '',
    credentialSecretKey: '',
    credentialSecretKeyVal: '',
  });

  const [azureSpecificData, setAzureSpecificData] = useState({
    clientIdKey: '',
    clientIdValue: '',
    clientSecretKey: '',
    clientSecretValue: '',
    tenantIdKey: '',
    tenantIdValue: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevState => ({ ...prevState, [name]: value }));
  };

  const handleProviderSpecificChange = (e) => {
    const { name, value } = e.target;
    if (cloudProvider === 'AWS') {
      setAwsSpecificData(prevState => ({ ...prevState, [name]: value }));
    } else {
      setAzureSpecificData(prevState => ({ ...prevState, [name]: value }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    let dataToSend = {
      userId: parseInt(localStorage.getItem('userId'), 10),
      driverName: formData.driverName,
      providerName: formData.providerName,
      driverLibFileName: formData.driverLibFileName,
      credentialName: formData.credentialName,
      credentialAccessKey: awsSpecificData.credentialAccessKey,
      credentialAccessKeyVal: awsSpecificData.credentialAccessKeyVal,
      credentialSecretKey: awsSpecificData.credentialSecretKey,
      credentialSecretKeyVal: awsSpecificData.credentialSecretKeyVal,
      regionName: formData.regionName,
      regionKey: formData.regionKey,
      regionValue: formData.regionValue,
      zoneKey: formData.zoneKey,
      zoneValue: formData.zoneValue,
      configName: formData.configName
    };

    try {
      const response = await fetch(`http://192.168.20.38:8080/api/cloud/${cloudProvider.toLowerCase()}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(dataToSend),
      });

      if (!response.ok) {
        throw new Error('Network response was not ok');
      }

      const result = await response.json();
      console.log('Success:', result);
      fetchExistingCloudInfo();
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const handleUpdate = async (e) => {
    e.preventDefault();
    let dataToSend = {
      userId: parseInt(localStorage.getItem('userId'), 10),
      driverName: formData.driverName,
      providerName: formData.providerName,
      driverLibFileName: formData.driverLibFileName,
      credentialName: formData.credentialName,
      regionName: formData.regionName,
      regionKey: formData.regionKey,
      regionValue: formData.regionValue,
      zoneKey: formData.zoneKey,
      zoneValue: formData.zoneValue,
      ...(cloudProvider === 'AWS' ? awsSpecificData : azureSpecificData)
    };

    try {
      const response = await fetch(`http://192.168.20.38:8080/api/cloud/${cloudProvider.toLowerCase()}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(dataToSend),
      });

      if (!response.ok) {
        throw new Error('Failed to update cloud info');
      }

      const result = await response.json();
      console.log('Success:', result);
      fetchExistingCloudInfo();
    } catch (error) {
      console.error('Error:', error);
    }
  };

  return (
    <form onSubmit={formType === 'create' ? handleSubmit : handleUpdate}>
      <select value={cloudProvider} onChange={(e) => setCloudProvider(e.target.value)}>
        <option value="AWS">AWS</option>
        <option value="AZURE">Azure</option>
      </select>

      <input type="text" name="driverName" value={formData.driverName} onChange={handleChange} placeholder="Driver Name" required />
      <input type="text" name="providerName" value={formData.providerName} onChange={handleChange} placeholder="Provider Name" required />
      <input type="text" name="driverLibFileName" value={formData.driverLibFileName} onChange={handleChange} placeholder="Driver Lib File Name" required />
      <input type="text" name="credentialName" value={formData.credentialName} onChange={handleChange} placeholder="Credential Name" required />
      <input type="text" name="regionName" value={formData.regionName} onChange={handleChange} placeholder="Region Name" required />
      <input type="text" name="regionKey" value={formData.regionKey} onChange={handleChange} placeholder="Region Key" required />
      <input type="text" name="regionValue" value={formData.regionValue} onChange={handleChange} placeholder="Region Value" required />
      <input type="text" name="zoneKey" value={formData.zoneKey} onChange={handleChange} placeholder="Zone Key" required />
      <input type="text" name="zoneValue" value={formData.zoneValue} onChange={handleChange} placeholder="Zone Value" required />
      <input type="text" name="configName" value={formData.configName} onChange={handleChange} placeholder="Config Name"required/>

      {cloudProvider === 'AWS' && (
        <>
          <input type="text" name="credentialAccessKey" value={awsSpecificData.credentialAccessKey} onChange={handleProviderSpecificChange} placeholder="Credential Access Key" required />
          <input type="text" name="credentialAccessKeyVal" value={awsSpecificData.credentialAccessKeyVal} onChange={handleProviderSpecificChange} placeholder="Credential Access Key Value" required />
          <input type="text" name="credentialSecretKey" value={awsSpecificData.credentialSecretKey} onChange={handleProviderSpecificChange} placeholder="Credential Secret Key" required />
          <input type="text" name="credentialSecretKeyVal" value={awsSpecificData.credentialSecretKeyVal} onChange={handleProviderSpecificChange} placeholder="Credential Secret Key Value" required />
        </>
      )}

      {cloudProvider === 'AZURE' && (
        <>
          <input type="text" name="clientIdKey" value={azureSpecificData.clientIdKey} onChange={handleProviderSpecificChange} placeholder="Client Id Key" required />
          <input type="text" name="clientIdValue" value={azureSpecificData.clientIdValue} onChange={handleProviderSpecificChange} placeholder="Client Id Value" required />
          <input type="text" name="clientSecretKey" value={azureSpecificData.clientSecretKey} onChange={handleProviderSpecificChange} placeholder="Client Secret Key" required />
          <input type="text" name="clientSecretValue" value={azureSpecificData.clientSecretValue} onChange={handleProviderSpecificChange} placeholder="Client Secret Value" required />
          <input type="text" name="tenantIdKey" value={azureSpecificData.tenantIdKey} onChange={handleProviderSpecificChange} placeholder="Tenant Id Key" required />
          <input type="text" name="tenantIdValue" value={azureSpecificData.tenantIdValue} onChange={handleProviderSpecificChange} placeholder="Tenant Id Value" required />
        </>
      )}

      <button type="submit" className="action-button">
        {formType === 'create' ? 'Submit Cloud Info' : 'Update Cloud Info'}
      </button>
    </form>
  );
}

export default CloudInfoForm;
const fileError = document.getElementById("file-error-message");
// displying original type section
const originalFileType = document.getElementById('original-type');
function setOrignialType(type){
    originalFileType.setAttribute("value",type);
    originalFileType.ariaPlaceholder=type;
    setOptions(type);
}

// file section
const fileInput = document.getElementById('original-file');
const fileLable = document.getElementById('file-label'); 

fileInput.addEventListener('change', ()=>{
    const fileSize = fileInput.files[0].size/1000;
    if(fileInput.files.length>0 && fileSize<=500){
        const fileName = fileInput.files[0].name;
        fileLable.textContent=fileName;
        console.log("size of "+ fileName+" is:"+fileSize);
        
        setOrignialType( fileName.split('.')[1].toLowerCase() )  ;
    }
    else{
        resetTodefualtValue();
        setError("لايمكنك تحويل ملف أكبر من 1 ميغابايت");
    }

});

function setError(Msg){
    fileError.textContent=Msg;
    fileError.parentElement.classList.replace("hide","show");
}

function resetTodefualtValue(){
    fileLable.textContent="أرفق الملف";
    originalFileType.setAttribute("value","");
    originalFileType.ariaPlaceholder="نوع الملف";

    optionConversionList.value=optionConversionList.options[0].value;
    resetOptionsIfThereIsAny();
}
// option section
const options = {
    pdf:["png","jpg"],
    png:["jpg","pdf"],
    jpg:["png","pdf"]
}
const optionConversionList = document.getElementById('conversion-type');

function resetOptionsIfThereIsAny(){
    const optionsN=optionConversionList.options.length;
    if(optionsN>1){
        let size =optionsN;
        for(let i = size; i>0; i--){
            optionConversionList.remove(i);
        }
    }
}

function setOptions(originalType)
{
    resetOptionsIfThereIsAny();
    if(options.hasOwnProperty(originalType)){

        options[originalType].forEach(element => {
           const optionEl = document.createElement('option');
           optionEl.value=element;
           optionEl.textContent=element;
           optionConversionList.appendChild(optionEl);
        });
    }

}


// submit validation
function validate(event){
    const isFileSet = hasFile();
    const isOriginalTypeSet = hasOriginalType();
    const isConversionTypeSet = hasConversionType();
    if(!isFileSet || !isOriginalTypeSet || !isConversionTypeSet){
        event.preventDefault();
        console.log("prevented")
    }
    console.log("is file uploaded:"+isFileSet+
    "\nis original set:"+isOriginalTypeSet+
    "\nis conversion type set:"+isConversionTypeSet
    )


}

function hasFile(){
    
    if(fileInput.files.length<=0){
        console.log(fileInput.files.length);
        return false;
    }
    return true;
}
function hasOriginalType(){
    
    if(originalFileType.value.length<=0){
    
        console.log(originalFileType.value.length);
        return false;
    }
    
    return true;
}
function hasConversionType(){
    
    if(optionConversionList.value.length<=0)
    {
        
        console.log(optionConversionList.value.length);
        return false;
    }
    
    return true;
}
// test Cases
const availableTestCases = {
    pdf:{
        types:["png","jpg"],
        defaultFile:"/static/images/default/test-pdf.pdf"
    },
    png:{
        types:["jpg"],
        defaultFile:"/static/images/default/test-png.png"
    },
    jpg:{
        types:["png"],
        defaultFile:"/static/images/default/test-jpg.jpg"
    }
}

function sendTest(original,conversion){
   if(availableTestCases.hasOwnProperty(original));
   {
        let defaultFile=null;
        availableTestCases[original].types.forEach(
            (element)=>{
                if( element==conversion)
                {
                    console.log("condition is met, try to send...", availableTestCases[original].defaultFile);
                    defaultFile= availableTestCases[original].defaultFile;
                    return;
                }
            }
        );
        console.log(defaultFile);

        post("/default-conversion",[{name:"original-type", value:original},{name:"conversion-type", value:conversion},{name:"file-path", value:defaultFile}])
    }
    // console.log(event.target.attributes.value);
    // const testCases = event.target;
}

/**
 * 
 * @param {string} path 
 * @param {Array} args 
 * @param {string} method 
 */
function post(path, args, method='post'){

    const body = document.body;
    const form =document.createElement('form');
    form.method=method;
    form.action=path;
    args.forEach(
        (element)=>{
            const hiddenInput = document.createElement('input');
            hiddenInput.hidden="hidden";
            hiddenInput.name=element.name;
            hiddenInput.value=element.value;

            console.log("adding:"+element.name+" : "+element.value);
            form.appendChild(hiddenInput);
        }
    );
        body.appendChild(form);
        form.submit();

}




/**
 * custom file for election's init/finish timestamps with Tempus Dominus lib
 */

const linkedPicker1Element = document.getElementById('linkedPickers1');
	const linked1 = new tempusDominus.TempusDominus(linkedPicker1Element, {
	      restrictions: {
	        minDate: new Date()
	      },
	      localization: {
	        startOfTheWeek: 1
	      },
	      display: {
	        buttons: {
	            close: true
	        }
	      }
	});
	const linked2 = new tempusDominus.TempusDominus(document.getElementById('linkedPickers2'), {
	      useCurrent: false,
	      localization: {
	        startOfTheWeek: 1
	      },
	      display: {
	        buttons: {
	            close: true
	        }
	      }
	});
	// event listener and handler
	linkedPicker1Element.addEventListener(tempusDominus.Namespace.events.change, (e) => {
	  linked2.updateOptions({
	    restrictions: {
	      minDate: e.detail.date
	    }
	  });
	});

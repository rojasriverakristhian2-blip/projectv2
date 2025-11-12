import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditActividad } from './edit-actividad';

describe('EditActividad', () => {
  let component: EditActividad;
  let fixture: ComponentFixture<EditActividad>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditActividad]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditActividad);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
